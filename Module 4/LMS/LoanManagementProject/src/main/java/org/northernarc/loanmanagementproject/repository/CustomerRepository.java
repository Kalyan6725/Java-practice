package org.northernarc.loanmanagementproject.repository;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByBranch(String branch);

    List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);

    List<Customer> findByBranchOrderByCustomerNameAsc(String branch);

    boolean existsByEmail(String email);

    List<Customer> findByEmailStartingWith(String emailPrefix);

    List<Customer> findByEmailEndingWith(String emailSuffix);

    long countByBranch(String branch);
    

    // Task 4.1: Find Premium Borrowers (customers with more than N loans)
    @Query("SELECT c FROM Customer c WHERE (SELECT COUNT(la) FROM LoanAccount la WHERE la.customer.customerId = c.customerId) > :loanCount")
    List<Customer> findPremiumBorrowers(@Param("loanCount") long loanCount);

    // Alternative: Find Premium Borrowers with Loan Statistics
    @Query("SELECT c FROM Customer c " +
           "LEFT JOIN c.loanAccounts la " +
           "GROUP BY c.customerId HAVING COUNT(la.loanAccountId) > :loanCount")
    List<Customer> findPremiumBorrowersWithCount(@Param("loanCount") long loanCount);

    // Task 4.3: Find Customers Using Multiple Loan Types (with COUNT(DISTINCT), GROUP BY, HAVING)
    @Query("SELECT c FROM Customer c " +
           "WHERE (SELECT COUNT(DISTINCT lp.loanType) FROM LoanAccount la " +
           "JOIN la.loanProduct lp WHERE la.customer.customerId = c.customerId) > 1")
    List<Customer> findCustomersWithMultipleLoanTypes();

    // Alternative: Find Customers with Specific Loan Type Count
    @Query("SELECT c, COUNT(DISTINCT lp.loanType) as loanTypeCount FROM Customer c " +
           "JOIN c.loanAccounts la " +
           "JOIN la.loanProduct lp " +
           "GROUP BY c.customerId HAVING COUNT(DISTINCT lp.loanType) >= :minLoanTypes " +
           "ORDER BY loanTypeCount DESC")
    List<Object[]> findCustomersWithLoanTypeStats(@Param("minLoanTypes") long minLoanTypes);

    // ==================== TASK 7: DTO PROJECTION MAPPING ====================
    
    // Task 7.1: Get Customer Summary with loan and penalty aggregates
    @Query("SELECT NEW org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO(" +
          "c.customerName, " +
          "c.branch, " +
          "CAST(COUNT(DISTINCT la.loanAccountId) AS long), " +
          "COALESCE(SUM(la.loanAmount), 0.0), " +
          "COALESCE(SUM(ep.penaltyPaid), 0.0)) " +
          "FROM Customer c " +
          "LEFT JOIN c.loanAccounts la " +
          "LEFT JOIN la.emiPayments ep " +
          "GROUP BY c.customerId, c.customerName, c.branch")
    List<CustomerSummaryDTO> findAllCustomerSummaries();

    // Task 7.2: Get Customer Summary by ID
    @Query("SELECT NEW org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO(" +
          "c.customerName, " +
          "c.branch, " +
          "CAST(COUNT(DISTINCT la.loanAccountId) AS long), " +
          "COALESCE(SUM(la.loanAmount), 0.0), " +
          "COALESCE(SUM(ep.penaltyPaid), 0.0)) " +
          "FROM Customer c " +
          "LEFT JOIN c.loanAccounts la " +
          "LEFT JOIN la.emiPayments ep " +
          "WHERE c.customerId = :customerId " +
          "GROUP BY c.customerId, c.customerName, c.branch")
    Optional<CustomerSummaryDTO> findCustomerSummaryById(@Param("customerId") Long customerId);

    // Task 7.3: Get Customer Summaries by Branch
    @Query("SELECT NEW org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO(" +
          "c.customerName, " +
          "c.branch, " +
          "CAST(COUNT(DISTINCT la.loanAccountId) AS long), " +
          "COALESCE(SUM(la.loanAmount), 0.0), " +
          "COALESCE(SUM(ep.penaltyPaid), 0.0)) " +
          "FROM Customer c " +
          "LEFT JOIN c.loanAccounts la " +
          "LEFT JOIN la.emiPayments ep " +
          "WHERE c.branch = :branch " +
          "GROUP BY c.customerId, c.customerName, c.branch " +
          "ORDER BY SUM(la.loanAmount) DESC")
    List<CustomerSummaryDTO> findCustomerSummariesByBranch(@Param("branch") String branch);

    // Task 7.4: Get Customer Summaries with minimum loan count
    @Query("SELECT NEW org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO(" +
          "c.customerName, " +
          "c.branch, " +
          "CAST(COUNT(DISTINCT la.loanAccountId) AS long), " +
          "COALESCE(SUM(la.loanAmount), 0.0), " +
          "COALESCE(SUM(ep.penaltyPaid), 0.0)) " +
          "FROM Customer c " +
          "LEFT JOIN c.loanAccounts la " +
          "LEFT JOIN la.emiPayments ep " +
          "GROUP BY c.customerId, c.customerName, c.branch " +
          "HAVING COUNT(DISTINCT la.loanAccountId) >= :minLoans " +
          "ORDER BY COUNT(DISTINCT la.loanAccountId) DESC")
    List<CustomerSummaryDTO> findCustomerSummariesWithMinLoans(@Param("minLoans") long minLoans);

    // ==================== FINAL CHALLENGE: DASHBOARD QUERIES ====================
    
    // Get total customer count
    @Query("SELECT COUNT(c) FROM Customer c")
    Long getTotalCustomerCount();
    
    // Get customer with highest loan amount
    @Query(value = "SELECT c.customer_name FROM customers c " +
           "LEFT JOIN loan_accounts la ON c.customer_id = la.customer_id " +
           "GROUP BY c.customer_id, c.customer_name " +
           "ORDER BY SUM(la.loan_amount) DESC LIMIT 1", nativeQuery = true)
    String getHighestLoanCustomer();
    
    // Get branch with most loans (top branch)
    @Query(value = "SELECT c.branch FROM customers c " +
           "LEFT JOIN loan_accounts la ON c.customer_id = la.customer_id " +
           "GROUP BY c.branch " +
           "ORDER BY COUNT(la.loan_account_id) DESC LIMIT 1", nativeQuery = true)
    String getTopBranch();
    
    // Data Initialization - Check if role exists
    boolean existsByRole(String role);
    
    // Data Initialization - Find all customers by role
    List<Customer> findByRole(String role);
}
