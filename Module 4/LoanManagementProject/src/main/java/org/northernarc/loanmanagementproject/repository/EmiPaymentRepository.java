package org.northernarc.loanmanagementproject.repository;

import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface EmiPaymentRepository extends JpaRepository<EmiPayment, Long> {
    

    @Query("SELECT ep FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :loanAccountId")
    List<EmiPayment> findByLoanAccountId(@Param("loanAccountId") Long loanAccountId);

    List<EmiPayment> findByPaymentType(String paymentType);

    List<EmiPayment> findByPaymentTypeOrderByPaymentDateDesc(String paymentType);

    @Query("SELECT ep FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :loanAccountId ORDER BY ep.paymentDate DESC")
    List<EmiPayment> findByLoanAccountIdOrderByPaymentDateDesc(@Param("loanAccountId") Long loanAccountId);

    List<EmiPayment> findByAmountPaidGreaterThan(Double amountPaid);

    List<EmiPayment> findByAmountPaidLessThan(Double amountPaid);

    List<EmiPayment> findByAmountPaidBetween(Double minAmount, Double maxAmount);

    List<EmiPayment> findByPenaltyPaidGreaterThan(Double penaltyAmount);

    List<EmiPayment> findByPenaltyPaidIsNull();

    List<EmiPayment> findByPaymentDateAfter(LocalDate paymentDate);

    List<EmiPayment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(ep) FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :loanAccountId")
    long countByLoanAccountId(@Param("loanAccountId") Long loanAccountId);

    long countByPaymentType(String paymentType);

    @Query("SELECT CASE WHEN COUNT(ep) > 0 THEN true ELSE false END FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :loanAccountId")
    boolean existsByLoanAccountId(@Param("loanAccountId") Long loanAccountId);

    @Query("SELECT ep FROM EmiPayment ep WHERE ep.paymentType = :paymentType AND ep.loanAccount.loanAccountId = :loanAccountId ORDER BY ep.paymentDate DESC")
    List<EmiPayment> findByPaymentTypeAndLoanAccountIdOrderByPaymentDateDesc(@Param("paymentType") String paymentType, @Param("loanAccountId") Long loanAccountId);

    @Query("SELECT ep FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :accountId AND ep.penaltyPaid > 0 ORDER BY ep.paymentDate DESC")
    List<EmiPayment> findPaymentsWithPenalties(@Param("accountId") Long accountId);

    @Query("SELECT SUM(ep.amountPaid) FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :accountId")
    Double getTotalAmountPaidByAccount(@Param("accountId") Long accountId);

    @Query("SELECT SUM(ep.penaltyPaid) FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :accountId")
    Double getTotalPenaltiesPaidByAccount(@Param("accountId") Long accountId);

    @Query("SELECT COUNT(ep) FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :accountId")
    long getPaymentCountByAccount(@Param("accountId") Long accountId);

    // Task 4.4: Find Latest EMI Payment (ORDER BY DESC with LIMIT equivalent)
    @Query("SELECT ep FROM EmiPayment ep ORDER BY ep.paymentDate DESC")
    List<EmiPayment> findLatestPaymentOrderByDesc();

    // Alternative: Find Latest Payment Per Account
    @Query("SELECT ep FROM EmiPayment ep WHERE ep.loanAccount.loanAccountId = :accountId " +
           "ORDER BY ep.paymentDate DESC")
    List<EmiPayment> findLatestPaymentPerAccount(@Param("accountId") Long accountId);

    // Alternative: Find Latest Payment Across All Accounts with Limit (using native query for LIMIT support)
    @Query(value = "SELECT * FROM emi_payment ORDER BY payment_date DESC LIMIT 1", nativeQuery = true)
    EmiPayment findMostRecentPayment();

    // Alternative: Find Latest Payment with Payment Details
    @Query("SELECT ep FROM EmiPayment ep WHERE ep.paymentDate = " +
           "(SELECT MAX(ep2.paymentDate) FROM EmiPayment ep2)")
    List<EmiPayment> findPaymentsOnLatestDate();

    // ==================== FINAL CHALLENGE: DASHBOARD QUERIES ====================
    
    // Get total penalty collected across all payments
    @Query("SELECT COALESCE(SUM(ep.penaltyPaid), 0.0) FROM EmiPayment ep")
    Double getTotalPenaltyCollected();
}

