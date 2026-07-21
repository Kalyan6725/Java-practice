package org.northernarc.loanmanagementproject.controller;

import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.LoanAccountResponse;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.service.LoanAccountService;
import org.northernarc.loanmanagementproject.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LoanAccountController - Sanctioned loans. USER sees only their own accounts;
 * MANAGER/ADMIN oversee and disburse.
 */
@RestController
@RequestMapping("/api/loan-accounts")
public class LoanAccountController {

    private final LoanAccountService loanAccountService;
    private final DTOMapper dtoMapper;
    private final SecurityUtils securityUtils;

    public LoanAccountController(LoanAccountService loanAccountService,
                               DTOMapper dtoMapper,
                               SecurityUtils securityUtils) {
        this.loanAccountService = loanAccountService;
        this.dtoMapper = dtoMapper;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanAccountResponse>>> myAccounts() {
        List<LoanAccountResponse> accounts = loanAccountService
                .getByCustomerId(securityUtils.currentCustomerId()).stream()
                .map(dtoMapper::toLoanAccountResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Your loan accounts fetched successfully", accounts));
    }

    @GetMapping("/{loanAccountId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<LoanAccountResponse>> getById(@PathVariable Long loanAccountId) {
        LoanAccount account = loanAccountService.getById(loanAccountId);
        enforceOwnerOrStaff(account);
        return ResponseEntity.ok(ApiResponse.success("Loan account fetched successfully",
                dtoMapper.toLoanAccountResponse(account)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanAccountResponse>>> getAll() {
        List<LoanAccountResponse> accounts = loanAccountService.getAll().stream()
                .map(dtoMapper::toLoanAccountResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Loan accounts fetched successfully", accounts));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanAccountResponse>>> byCustomer(@PathVariable Long customerId) {
        if (!securityUtils.isStaff() && !customerId.equals(securityUtils.currentCustomerId())) {
            throw new AccessDeniedException("You can only access your own loan accounts");
        }
        List<LoanAccountResponse> accounts = loanAccountService.getByCustomerId(customerId).stream()
                .map(dtoMapper::toLoanAccountResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Customer loan accounts fetched successfully", accounts));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanAccountResponse>>> byStatus(@PathVariable String status) {
        List<LoanAccountResponse> accounts = loanAccountService.getByStatus(status).stream()
                .map(dtoMapper::toLoanAccountResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Loan accounts fetched successfully", accounts));
    }

    @PostMapping("/{loanAccountId}/disburse")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<LoanAccountResponse>> disburse(@PathVariable Long loanAccountId) {
        LoanAccount disbursed = loanAccountService.disburse(loanAccountId);
        return ResponseEntity.ok(ApiResponse.success("Loan disbursed successfully",
                dtoMapper.toLoanAccountResponse(disbursed)));
    }

    private void enforceOwnerOrStaff(LoanAccount account) {
        if (securityUtils.isStaff()) {
            return;
        }
        Long ownerId = account.getCustomer() != null ? account.getCustomer().getCustomerId() : null;
        if (ownerId == null || !ownerId.equals(securityUtils.currentCustomerId())) {
            throw new AccessDeniedException("You can only access your own loan accounts");
        }
    }
}
