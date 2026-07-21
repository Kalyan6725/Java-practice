package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.northernarc.loanmanagementproject.dto.request.AdminCustomerDTO;
import org.northernarc.loanmanagementproject.dto.request.CustomerUpdateRequest;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.CustomerResponse;
import org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.service.CustomerService;
import org.northernarc.loanmanagementproject.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CustomerController - Customer management.
 * ADMIN performs full CRUD; a USER may view/update only their own profile;
 * MANAGER/ADMIN can view summaries.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final DTOMapper dtoMapper;
    private final SecurityUtils securityUtils;

    public CustomerController(CustomerService customerService,
                             CustomerRepository customerRepository,
                             DTOMapper dtoMapper,
                             SecurityUtils securityUtils) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.dtoMapper = dtoMapper;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@Valid @RequestBody AdminCustomerDTO dto) {
        Customer saved = customerService.createByAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", dtoMapper.toCustomerResponse(saved)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {
        List<CustomerResponse> customers = customerService.getAll().stream()
                .map(dtoMapper::toCustomerResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Customers fetched successfully", customers));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable Long customerId) {
        enforceSelfOrStaff(customerId);
        Customer customer = customerService.getById(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully",
                dtoMapper.toCustomerResponse(customer)));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CustomerResponse>> getMe() {
        Customer me = securityUtils.currentCustomer();
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully",
                dtoMapper.toCustomerResponse(me)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(@Valid @RequestBody CustomerUpdateRequest request) {
        Customer updated = customerService.update(request, true);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully",
                dtoMapper.toCustomerResponse(updated)));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateMe(@Valid @RequestBody CustomerUpdateRequest request) {
        request.setCustomerId(securityUtils.currentCustomerId());
        Customer updated = customerService.update(request, false);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully",
                dtoMapper.toCustomerResponse(updated)));
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long customerId) {
        customerService.delete(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully"));
    }

    @GetMapping("/summaries")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerSummaryDTO>>> getAllSummaries() {
        return ResponseEntity.ok(ApiResponse.success("Customer summaries fetched successfully",
                customerRepository.findAllCustomerSummaries()));
    }

    @GetMapping("/{customerId}/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CustomerSummaryDTO>> getSummary(@PathVariable Long customerId) {
        enforceSelfOrStaff(customerId);
        CustomerSummaryDTO summary = customerRepository.findCustomerSummaryById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        return ResponseEntity.ok(ApiResponse.success("Customer summary fetched successfully", summary));
    }

    @GetMapping("/branch/{branch}/summaries")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerSummaryDTO>>> getSummariesByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(ApiResponse.success("Branch customer summaries fetched successfully",
                customerRepository.findCustomerSummariesByBranch(branch)));
    }

    @GetMapping("/summaries/min-loans")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerSummaryDTO>>> getSummariesWithMinLoans(
            @RequestParam(value = "minLoans", defaultValue = "1") long minLoans) {
        return ResponseEntity.ok(ApiResponse.success("Customer summaries fetched successfully",
                customerRepository.findCustomerSummariesWithMinLoans(minLoans)));
    }

    private void enforceSelfOrStaff(Long customerId) {
        if (securityUtils.isStaff()) {
            return;
        }
        if (!customerId.equals(securityUtils.currentCustomerId())) {
            throw new AccessDeniedException("You can only access your own customer record");
        }
    }
}
