package org.northernarc.loanmanagementproject.controller;

import org.northernarc.loanmanagementproject.service.LoanService;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.dto.CustomerSummaryDTO;
import org.northernarc.loanmanagementproject.dto.DashboardDTO;
import org.northernarc.loanmanagementproject.dto.AdminCustomerDTO;
import org.northernarc.loanmanagementproject.dto.ApiResponse;
import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin(origins = "*")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanProductRepository loanProductRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Customer Endpoints
    /**
     * ADMIN creates customer (bulk import/management)
     * Password is encoded here before saving
     * Admin can specify custom role (not limited to USER)
     * 
     * @param dto AdminCustomerDTO with customer details
     * @return Created customer
     */
    @PostMapping("/customer/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody AdminCustomerDTO dto) {
        // Check if email already exists
        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }
        
        // Create Customer entity from DTO
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword())); // ✅ ENCODE HERE
        customer.setBranch(dto.getBranch());
        customer.setRole(dto.getRole() != null ? dto.getRole() : "USER");
        
        // Save and return
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerRepository.save(customer));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getCustomerById(@PathVariable Long customerId) {
        Customer customer = loanService.getCustomerById(customerId);
        return ResponseEntity.ok(ApiResponse.success(200, "Customer fetched successfully", customer));
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(loanService.getAllCustomers());
    }

    @PutMapping("/customer/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customer) {
        return ResponseEntity.ok(loanService.updateCustomer(customer));
    }

    @DeleteMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        loanService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/summaries")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerSummaryDTO>> getAllCustomerSummaries() {
        List<CustomerSummaryDTO> summaries = customerRepository.findAllCustomerSummaries();
        return ResponseEntity.ok(summaries);
    }

    // Task 7.2: Get customer summary by ID
    @GetMapping("/customer/{customerId}/summary")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerSummaryDTO> getCustomerSummary(@PathVariable Long customerId) {
        Optional<CustomerSummaryDTO> summary = customerRepository.findCustomerSummaryById(customerId);
        return summary.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Task 7.3: Get customer summaries by branch
    @GetMapping("/customers/branch/{branch}/summaries")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomerSummariesByBranch(@PathVariable String branch) {
        List<CustomerSummaryDTO> summaries = customerRepository.findCustomerSummariesByBranch(branch);
        return ResponseEntity.ok(summaries);
    }

    // Task 7.4: Get customer summaries with minimum loan count
    @GetMapping("/customers/summaries/min-loans")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomerSummariesWithMinLoans(
            @RequestParam(value = "minLoans", defaultValue = "1") long minLoans) {
        List<CustomerSummaryDTO> summaries = customerRepository.findCustomerSummariesWithMinLoans(minLoans);
        return ResponseEntity.ok(summaries);
    }

    @PostMapping("/product/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanProduct> createLoanProduct(@Valid @RequestBody LoanProduct loanProduct) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoanProduct(loanProduct));
    }

    @GetMapping("/product/{loanCode}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LoanProduct> getLoanProductByCode(@PathVariable String loanCode) {
        LoanProduct product = loanService.getLoanProductByCode(loanCode);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<LoanProduct>> getAllLoanProducts() {
        return ResponseEntity.ok(loanService.getAllLoanProducts());
    }

    // Task 6: Pagination & Sorting endpoint
    // GET /api/loan/loan-products?page=0&size=10&sort=dailyPenaltyRate,desc
    @GetMapping("/loan-products")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Page<LoanProduct>> getLoanProductsWithPagination(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dailyPenaltyRate") String sortBy) {
        
        // Create Pageable with default sort by penalty rate descending
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, 
            Sort.by(Sort.Direction.DESC, sortBy));
        
        Page<LoanProduct> products = loanProductRepository.findAllProductsSortedByPenaltyRate(pageable);
        return ResponseEntity.ok(products);
    }

    // Task 6.1: Alternative endpoint - Pagination with custom sorting
    @GetMapping("/loan-products/search")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Page<LoanProduct>> searchLoanProducts(
            @RequestParam(value = "loanType", required = false) String loanType,
            @RequestParam(value = "loanName", required = false) String loanName,
            @RequestParam(value = "minPenalty", required = false) Double minPenalty,
            @RequestParam(value = "maxPenalty", required = false) Double maxPenalty,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "dailyPenaltyRate") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction) {
        
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, 
            Sort.by(direction, sortBy));
        
        // Handle different search scenarios
        if (loanType != null && !loanType.isEmpty()) {
            return ResponseEntity.ok(loanProductRepository.findByLoanType(loanType, pageable));
        }
        
        if (loanName != null && !loanName.isEmpty()) {
            return ResponseEntity.ok(loanProductRepository.findByLoanNameContainingIgnoreCase(loanName, pageable));
        }
        
        if (minPenalty != null && maxPenalty != null) {
            return ResponseEntity.ok(loanProductRepository.findByDailyPenaltyRateBetween(minPenalty, maxPenalty, pageable));
        }
        
        if (minPenalty != null) {
            return ResponseEntity.ok(loanProductRepository.findByDailyPenaltyRateGreaterThan(minPenalty, pageable));
        }
        
        // Default: return all with sorting
        return ResponseEntity.ok(loanProductRepository.findAllProductsSortedByPenaltyRate(pageable));
    }

    @PutMapping("/product/update")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LoanProduct> updateLoanProduct(@Valid @RequestBody LoanProduct loanProduct) {
        return ResponseEntity.ok(loanService.updateLoanProduct(loanProduct));
    }

    @DeleteMapping("/product/{loanCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLoanProduct(@PathVariable String loanCode) {
        loanService.deleteLoanProduct(loanCode);
        return ResponseEntity.noContent().build();
    }

    // Loan Account Endpoints
    @PostMapping("/account/create")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LoanAccount> createLoanAccount(@Valid @RequestBody LoanAccount loanAccount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoanAccount(loanAccount));
    }

    @GetMapping("/account/{loanAccountId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LoanAccount> getLoanAccountById(@PathVariable Long loanAccountId) {
        LoanAccount account = loanService.getLoanAccountById(loanAccountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<LoanAccount>> getAllLoanAccounts() {
        return ResponseEntity.ok(loanService.getAllLoanAccounts());
    }

    @GetMapping("/accounts/customer/{customerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<LoanAccount>> getLoanAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(loanService.getLoanAccountsByCustomerId(customerId));
    }

    @GetMapping("/accounts/status/{status}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<LoanAccount>> getLoanAccountsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(loanService.getLoanAccountsByStatus(status));
    }

    @PutMapping("/account/update")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LoanAccount> updateLoanAccount(@Valid @RequestBody LoanAccount loanAccount) {
        return ResponseEntity.ok(loanService.updateLoanAccount(loanAccount));
    }

    @DeleteMapping("/account/{loanAccountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLoanAccount(@PathVariable Long loanAccountId) {
        loanService.deleteLoanAccount(loanAccountId);
        return ResponseEntity.noContent().build();
    }

    // EMI Payment Endpoints
    @PostMapping("/payment/record")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmiPayment> recordEmiPayment(@Valid @RequestBody EmiPayment emiPayment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.recordEmiPayment(emiPayment));
    }

    @GetMapping("/payment/{paymentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmiPayment> getEmiPaymentById(@PathVariable Long paymentId) {
        EmiPayment payment = loanService.getEmiPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/payments")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmiPayment>> getAllEmiPayments() {
        return ResponseEntity.ok(loanService.getAllEmiPayments());
    }

    @GetMapping("/payments/account/{loanAccountId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmiPayment>> getEmiPaymentsByLoanAccountId(@PathVariable Long loanAccountId) {
        return ResponseEntity.ok(loanService.getEmiPaymentsByLoanAccountId(loanAccountId));
    }

    @GetMapping("/payments/type/{paymentType}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmiPayment>> getEmiPaymentsByPaymentType(@PathVariable String paymentType) {
        return ResponseEntity.ok(loanService.getEmiPaymentsByPaymentType(paymentType));
    }

    @PutMapping("/payment/update")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmiPayment> updateEmiPayment(@Valid @RequestBody EmiPayment emiPayment) {
        return ResponseEntity.ok(loanService.updateEmiPayment(emiPayment));
    }

    @DeleteMapping("/payment/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmiPayment(@PathVariable Long paymentId) {
        loanService.deleteEmiPayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    // ==================== FINAL CHALLENGE: DASHBOARD ====================
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getDashboard() {
        DashboardDTO dashboardData = loanService.getDashboardData();
        return ResponseEntity.ok(
            ApiResponse.success(200, "Dashboard data retrieved successfully", dashboardData)
        );
    }
}
