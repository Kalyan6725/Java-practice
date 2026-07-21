package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.northernarc.loanmanagementproject.dto.request.EmiPaymentRequest;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.EmiPaymentResponse;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.service.EmiPaymentService;
import org.northernarc.loanmanagementproject.service.LoanAccountService;
import org.northernarc.loanmanagementproject.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EmiPaymentController - EMI payments and schedule.
 * USER pays/views their own loan's EMIs; MANAGER/ADMIN oversee all.
 */
@RestController
@RequestMapping("/api/emi-payments")
public class EmiPaymentController {

    private final EmiPaymentService emiPaymentService;
    private final LoanAccountService loanAccountService;
    private final DTOMapper dtoMapper;
    private final SecurityUtils securityUtils;

    public EmiPaymentController(EmiPaymentService emiPaymentService,
                              LoanAccountService loanAccountService,
                              DTOMapper dtoMapper,
                              SecurityUtils securityUtils) {
        this.emiPaymentService = emiPaymentService;
        this.loanAccountService = loanAccountService;
        this.dtoMapper = dtoMapper;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<EmiPaymentResponse>> pay(@Valid @RequestBody EmiPaymentRequest request) {
        enforceAccountOwnerOrStaff(request.getLoanAccountId());
        EmiPayment saved = emiPaymentService.pay(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("EMI payment recorded successfully",
                        dtoMapper.toEmiPaymentResponse(saved)));
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<EmiPaymentResponse>> getById(@PathVariable Long paymentId) {
        EmiPayment payment = emiPaymentService.getById(paymentId);
        if (payment.getLoanAccount() != null) {
            enforceAccountOwnerOrStaff(payment.getLoanAccount().getLoanAccountId());
        }
        return ResponseEntity.ok(ApiResponse.success("EMI payment fetched successfully",
                dtoMapper.toEmiPaymentResponse(payment)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<EmiPaymentResponse>>> getAll() {
        List<EmiPaymentResponse> payments = emiPaymentService.getAll().stream()
                .map(dtoMapper::toEmiPaymentResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("EMI payments fetched successfully", payments));
    }

    @GetMapping("/account/{loanAccountId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<EmiPaymentResponse>>> byAccount(@PathVariable Long loanAccountId) {
        enforceAccountOwnerOrStaff(loanAccountId);
        List<EmiPaymentResponse> payments = emiPaymentService.getByLoanAccountId(loanAccountId).stream()
                .map(dtoMapper::toEmiPaymentResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("EMI payments fetched successfully", payments));
    }

    @GetMapping("/account/{loanAccountId}/schedule")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<EmiPaymentResponse>>> schedule(@PathVariable Long loanAccountId) {
        enforceAccountOwnerOrStaff(loanAccountId);
        return ResponseEntity.ok(ApiResponse.success("EMI schedule fetched successfully",
                emiPaymentService.getSchedule(loanAccountId)));
    }

    private void enforceAccountOwnerOrStaff(Long loanAccountId) {
        if (securityUtils.isStaff()) {
            return;
        }
        LoanAccount account = loanAccountService.getById(loanAccountId);
        Long ownerId = account.getCustomer() != null ? account.getCustomer().getCustomerId() : null;
        if (ownerId == null || !ownerId.equals(securityUtils.currentCustomerId())) {
            throw new AccessDeniedException("You can only access EMIs for your own loans");
        }
    }
}
