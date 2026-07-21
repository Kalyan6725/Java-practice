package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.northernarc.loanmanagementproject.dto.request.LoanApplicationRequest;
import org.northernarc.loanmanagementproject.dto.request.LoanApplicationReviewRequest;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.LoanApplicationResponse;
import org.northernarc.loanmanagementproject.entity.LoanApplication;
import org.northernarc.loanmanagementproject.service.LoanApplicationService;
import org.northernarc.loanmanagementproject.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LoanApplicationController - The loan workflow surface.
 * USER applies and tracks; UNDERWRITER reviews; MANAGER/ADMIN oversee.
 */
@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final DTOMapper dtoMapper;
    private final SecurityUtils securityUtils;

    public LoanApplicationController(LoanApplicationService loanApplicationService,
                                    DTOMapper dtoMapper,
                                    SecurityUtils securityUtils) {
        this.loanApplicationService = loanApplicationService;
        this.dtoMapper = dtoMapper;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> apply(
            @Valid @RequestBody LoanApplicationRequest request) {
        Long applicantId = securityUtils.currentCustomerId();
        // A plain USER may only apply for themselves.
        if (!securityUtils.isStaff()) {
            request.setCustomerId(applicantId);
        }
        LoanApplication saved = loanApplicationService.apply(request, applicantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Loan application submitted successfully",
                        dtoMapper.toLoanApplicationResponse(saved)));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> myApplications() {
        List<LoanApplicationResponse> applications = loanApplicationService
                .getByCustomer(securityUtils.currentCustomerId()).stream()
                .map(dtoMapper::toLoanApplicationResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Your loan applications fetched successfully", applications));
    }

    @GetMapping("/{applicationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> getById(@PathVariable Long applicationId) {
        LoanApplication application = loanApplicationService.getById(applicationId);
        enforceOwnerOrStaff(application);
        return ResponseEntity.ok(ApiResponse.success("Loan application fetched successfully",
                dtoMapper.toLoanApplicationResponse(application)));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('UNDERWRITER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> byCustomer(@PathVariable Long customerId) {
        List<LoanApplicationResponse> applications = loanApplicationService.getByCustomer(customerId).stream()
                .map(dtoMapper::toLoanApplicationResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Customer applications fetched successfully", applications));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('UNDERWRITER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAll(
            @RequestParam(value = "status", required = false) String status) {
        List<LoanApplication> applications = (status != null && !status.isEmpty())
                ? loanApplicationService.getByStatus(status)
                : loanApplicationService.getAll();
        List<LoanApplicationResponse> body = applications.stream()
                .map(dtoMapper::toLoanApplicationResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Loan applications fetched successfully", body));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('UNDERWRITER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> pendingQueue() {
        List<LoanApplicationResponse> queue = loanApplicationService.getOpenQueue().stream()
                .map(dtoMapper::toLoanApplicationResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Pending applications fetched successfully", queue));
    }

    @PutMapping("/{applicationId}/review")
    @PreAuthorize("hasAnyRole('UNDERWRITER','ADMIN')")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> review(
            @PathVariable Long applicationId,
            @Valid @RequestBody LoanApplicationReviewRequest request) {
        LoanApplication reviewed = loanApplicationService.review(applicationId, request);
        return ResponseEntity.ok(ApiResponse.success("Loan application reviewed successfully",
                dtoMapper.toLoanApplicationResponse(reviewed)));
    }

    private void enforceOwnerOrStaff(LoanApplication application) {
        if (securityUtils.isStaff()) {
            return;
        }
        Long ownerId = application.getCustomer() != null ? application.getCustomer().getCustomerId() : null;
        if (ownerId == null || !ownerId.equals(securityUtils.currentCustomerId())) {
            throw new AccessDeniedException("You can only access your own loan applications");
        }
    }
}
