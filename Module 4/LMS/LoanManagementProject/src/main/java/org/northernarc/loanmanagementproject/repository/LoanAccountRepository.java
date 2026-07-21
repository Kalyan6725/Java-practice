package org.northernarc.loanmanagementproject.repository;

import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Long> {

    @Query("SELECT la FROM LoanAccount la WHERE la.customer.customerId = :customerId")
    List<LoanAccount> findByCustomerId(@Param("customerId") Long customerId);

    List<LoanAccount> findByStatus(String status);

    List<LoanAccount> findByStatusOrderByLoanStartDateDesc(String status);

    @Query("SELECT la FROM LoanAccount la WHERE la.customer.customerId = :customerId AND la.status = :status")
    List<LoanAccount> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") String status);

    List<LoanAccount> findByLoanProduct_LoanCode(String loanCode);

    List<LoanAccount> findByLoanAmountGreaterThan(Double loanAmount);

    List<LoanAccount> findByLoanAmountLessThan(Double loanAmount);

    List<LoanAccount> findByLoanAmountBetween(Double minAmount, Double maxAmount);

    List<LoanAccount> findByStatusAndEmiDueDateBefore(String status, LocalDate dueDate);

    long countByStatus(String status);

    @Query("SELECT COUNT(la) FROM LoanAccount la WHERE la.customer.customerId = :customerId AND la.status = :status")
    long countByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") String status);

    @Query("SELECT CASE WHEN COUNT(la) > 0 THEN true ELSE false END FROM LoanAccount la WHERE la.customer.customerId = :customerId AND la.status = :status")
    boolean existsByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") String status);

    //List<LoanAccount> findByCreatedAtAfterOrderByCreatedAtDesc(java.time.LocalDateTime createdAt);

    @Query("SELECT la FROM LoanAccount la WHERE la.status = 'ACTIVE' ORDER BY la.loanStartDate DESC")
    List<LoanAccount> findAllActiveAccounts();

    @Query("SELECT la FROM LoanAccount la WHERE la.customer.customerId = :customerId AND la.status = 'OVERDUE'")
    List<LoanAccount> findOverdueLoansByCustomer(@Param("customerId") Long customerId);

    @Query("SELECT SUM(la.loanAmount) FROM LoanAccount la WHERE la.customer.customerId = :customerId AND la.status = 'ACTIVE'")
    Double getTotalActiveLoanAmountByCustomer(@Param("customerId") Long customerId);

    // Task 4.2: Find Total Penalty Collected Per Branch (with JOIN and GROUP BY)
    @Query("SELECT c.branch, SUM(ep.penaltyPaid) FROM Customer c " +
           "JOIN c.loanAccounts la " +
           "JOIN la.emiPayments ep " +
           "GROUP BY c.branch ORDER BY SUM(ep.penaltyPaid) DESC")
    List<Object[]> findTotalPenaltyPerBranch();

    // Alternative: Find Total Penalty Collected Per Branch with Product Type Filter
    @Query("SELECT c.branch, COUNT(DISTINCT lp.loanType) as loanTypeCount, SUM(ep.penaltyPaid) as totalPenalty " +
           "FROM Customer c " +
           "JOIN c.loanAccounts la " +
           "JOIN la.loanProduct lp " +
           "JOIN la.emiPayments ep " +
           "GROUP BY c.branch " +
           "ORDER BY totalPenalty DESC")
    List<Object[]> findBranchPenaltyStats();

    // ==================== FINAL CHALLENGE: DASHBOARD QUERIES ====================
    
    // Get total loan accounts count
    @Query("SELECT COUNT(la) FROM LoanAccount la")
    Long getTotalLoanCount();
    
    // Get total loan amount disbursed
    @Query("SELECT COALESCE(SUM(la.loanAmount), 0.0) FROM LoanAccount la")
    Double getTotalLoanAmountDisbursed();
}

