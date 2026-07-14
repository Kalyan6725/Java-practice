package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.request.AdminCustomerDTO;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO;
import org.northernarc.loanmanagementproject.dto.response.DashboardDTO;
import org.northernarc.loanmanagementproject.dto.response.PagedData;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.northernarc.loanmanagementproject.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private PasswordEncoder passwordEncoder;

    @PostMapping("/customer/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@Valid @RequestBody AdminCustomerDTO dto) {
        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Email already registered", "BAD_REQUEST", null));
        }

        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setBranch(dto.getBranch());
        customer.setRole(dto.getRole() != null ? dto.getRole() : "USER");

        Customer saved = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", saved));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable Long customerId) {
        Customer customer = loanService.getCustomerById(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
        return ResponseEntity.ok(ApiResponse.success("Customers fetched successfully", loanService.getAllCustomers()));
    }

    @PutMapping("/customer/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@Valid @RequestBody Customer customer) {
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", loanService.updateCustomer(customer)));
    }

    @DeleteMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long customerId) {
        loanService.deleteCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully"));
    }

    @GetMapping("/customers/summaries")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerSummaryDTO>>> getAllCustomerSummaries() {
        List<CustomerSummaryDTO> summaries = customerRepository.findAllCustomerSummaries();
        return ResponseEntity.ok(ApiResponse.success("Customer summaries fetched successfully", summaries));
    }

    @GetMapping("/customer/{customerId}/summary")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerSummaryDTO>> getCustomerSummary(@PathVariable Long customerId) {
        CustomerSummaryDTO summary = customerRepository.findCustomerSummaryById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        return ResponseEntity.ok(ApiResponse.success("Customer summary fetched successfully", summary));
    }

    @GetMapping("/customers/branch/{branch}/summaries")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerSummaryDTO>>> getCustomerSummariesByBranch(@PathVariable String branch) {
        List<CustomerSummaryDTO> summaries = customerRepository.findCustomerSummariesByBranch(branch);
        return ResponseEntity.ok(ApiResponse.success("Branch customer summaries fetched successfully", summaries));
    }

    @GetMapping("/customers/summaries/min-loans")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CustomerSummaryDTO>>> getCustomerSummariesWithMinLoans(
            @RequestParam(value = "minLoans", defaultValue = "1") long minLoans) {
        List<CustomerSummaryDTO> summaries = customerRepository.findCustomerSummariesWithMinLoans(minLoans);
        return ResponseEntity.ok(ApiResponse.success("Customer summaries fetched successfully", summaries));
    }

    @PostMapping("/product/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanProduct>> createLoanProduct(@Valid @RequestBody LoanProduct loanProduct) {
        LoanProduct saved = loanService.createLoanProduct(loanProduct);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Loan product created successfully", saved));
    }

    @GetMapping("/product/{loanCode}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanProduct>> getLoanProductByCode(@PathVariable String loanCode) {
        LoanProduct product = loanService.getLoanProductByCode(loanCode);
        return ResponseEntity.ok(ApiResponse.success("Loan product fetched successfully", product));
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanProduct>>> getAllLoanProducts() {
        return ResponseEntity.ok(ApiResponse.success("Loan products fetched successfully", loanService.getAllLoanProducts()));
    }

    @GetMapping("/loan-products")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagedData<LoanProduct>>> getLoanProductsWithPagination(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dailyPenaltyRate") String sortBy) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<LoanProduct> products = loanProductRepository.findAllProductsSortedByPenaltyRate(pageable);
        return ResponseEntity.ok(ApiResponse.success("Loan products fetched successfully", toPagedData(products)));
    }

    @GetMapping("/loan-products/search")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagedData<LoanProduct>>> searchLoanProducts(
            @RequestParam(value = "loanType", required = false) String loanType,
            @RequestParam(value = "loanName", required = false) String loanName,
            @RequestParam(value = "minPenalty", required = false) Double minPenalty,
            @RequestParam(value = "maxPenalty", required = false) Double maxPenalty,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "dailyPenaltyRate") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LoanProduct> products;

        if (loanType != null && !loanType.isEmpty()) {
            products = loanProductRepository.findByLoanType(loanType, pageable);
        } else if (loanName != null && !loanName.isEmpty()) {
            products = loanProductRepository.findByLoanNameContainingIgnoreCase(loanName, pageable);
        } else if (minPenalty != null && maxPenalty != null) {
            products = loanProductRepository.findByDailyPenaltyRateBetween(minPenalty, maxPenalty, pageable);
        } else if (minPenalty != null) {
            products = loanProductRepository.findByDailyPenaltyRateGreaterThan(minPenalty, pageable);
        } else {
            products = loanProductRepository.findAllProductsSortedByPenaltyRate(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success("Loan products fetched successfully", toPagedData(products)));
    }

    @PutMapping("/product/update")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanProduct>> updateLoanProduct(@Valid @RequestBody LoanProduct loanProduct) {
        return ResponseEntity.ok(ApiResponse.success("Loan product updated successfully", loanService.updateLoanProduct(loanProduct)));
    }

    @DeleteMapping("/product/{loanCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLoanProduct(@PathVariable String loanCode) {
        loanService.deleteLoanProduct(loanCode);
        return ResponseEntity.ok(ApiResponse.success("Loan product deleted successfully"));
    }

    @PostMapping("/account/create")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanAccount>> createLoanAccount(@Valid @RequestBody LoanAccount loanAccount) {
        LoanAccount saved = loanService.createLoanAccount(loanAccount);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Loan account created successfully", saved));
    }

    @GetMapping("/account/{loanAccountId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanAccount>> getLoanAccountById(@PathVariable Long loanAccountId) {
        LoanAccount account = loanService.getLoanAccountById(loanAccountId);
        return ResponseEntity.ok(ApiResponse.success("Loan account fetched successfully", account));
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanAccount>>> getAllLoanAccounts() {
        return ResponseEntity.ok(ApiResponse.success("Loan accounts fetched successfully", loanService.getAllLoanAccounts()));
    }

    @GetMapping("/accounts/customer/{customerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanAccount>>> getLoanAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.success("Customer loan accounts fetched successfully", loanService.getLoanAccountsByCustomerId(customerId)));
    }

    @GetMapping("/accounts/status/{status}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanAccount>>> getLoanAccountsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.success("Loan accounts fetched successfully", loanService.getLoanAccountsByStatus(status)));
    }

    @PutMapping("/account/update")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanAccount>> updateLoanAccount(@Valid @RequestBody LoanAccount loanAccount) {
        return ResponseEntity.ok(ApiResponse.success("Loan account updated successfully", loanService.updateLoanAccount(loanAccount)));
    }

    @DeleteMapping("/account/{loanAccountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLoanAccount(@PathVariable Long loanAccountId) {
        loanService.deleteLoanAccount(loanAccountId);
        return ResponseEntity.ok(ApiResponse.success("Loan account deleted successfully"));
    }

    @PostMapping("/payment/record")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmiPayment>> recordEmiPayment(@Valid @RequestBody EmiPayment emiPayment) {
        EmiPayment saved = loanService.recordEmiPayment(emiPayment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("EMI payment recorded successfully", saved));
    }

    @GetMapping("/payment/{paymentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmiPayment>> getEmiPaymentById(@PathVariable Long paymentId) {
        EmiPayment payment = loanService.getEmiPaymentById(paymentId);
        return ResponseEntity.ok(ApiResponse.success("EMI payment fetched successfully", payment));
    }

    @GetMapping("/payments")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmiPayment>>> getAllEmiPayments() {
        return ResponseEntity.ok(ApiResponse.success("EMI payments fetched successfully", loanService.getAllEmiPayments()));
    }

    @GetMapping("/payments/account/{loanAccountId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmiPayment>>> getEmiPaymentsByLoanAccountId(@PathVariable Long loanAccountId) {
        return ResponseEntity.ok(ApiResponse.success("EMI payments fetched successfully", loanService.getEmiPaymentsByLoanAccountId(loanAccountId)));
    }

    @GetMapping("/payments/type/{paymentType}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmiPayment>>> getEmiPaymentsByPaymentType(@PathVariable String paymentType) {
        return ResponseEntity.ok(ApiResponse.success("EMI payments fetched successfully", loanService.getEmiPaymentsByPaymentType(paymentType)));
    }

    @PutMapping("/payment/update")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmiPayment>> updateEmiPayment(@Valid @RequestBody EmiPayment emiPayment) {
        return ResponseEntity.ok(ApiResponse.success("EMI payment updated successfully", loanService.updateEmiPayment(emiPayment)));
    }

    @DeleteMapping("/payment/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmiPayment(@PathVariable Long paymentId) {
        loanService.deleteEmiPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("EMI payment deleted successfully"));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard() {
        DashboardDTO dashboardData = loanService.getDashboardData();
        return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved successfully", dashboardData));
    }

    private <T> PagedData<T> toPagedData(Page<T> page) {
        return PagedData.<T>builder()
                .items(page.getContent())
                .page(PagedData.PageMeta.builder()
                        .number(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .build())
                .build();
    }
}
