package org.northernarc.loanmanagementproject.repository;

import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, String> {

    Optional<LoanProduct> findByLoanName(String loanName);

    List<LoanProduct> findByLoanType(String loanType);

    List<LoanProduct> findByDailyPenaltyRateGreaterThan(Double dailyPenaltyRate);

    List<LoanProduct> findByDailyPenaltyRateLessThanEqual(Double dailyPenaltyRate);

    List<LoanProduct> findByDailyPenaltyRateBetween(Double minRate, Double maxRate);

    List<LoanProduct> findByLoanTypeOrderByLoanNameAsc(String loanType);

    List<LoanProduct> findByLoanTypeOrderByDailyPenaltyRateDesc(String loanType);

    List<LoanProduct> findByLoanNameContainingIgnoreCase(String loanName);

    boolean existsByLoanCode(String loanCode);

    long countByLoanType(String loanType);

    @Query("SELECT lp FROM LoanProduct lp ORDER BY lp.dailyPenaltyRate ASC")
    List<LoanProduct> findAllOrderedByPenaltyRate();

    @Query("SELECT lp FROM LoanProduct lp WHERE lp.dailyPenaltyRate > :penaltyRate ORDER BY lp.dailyPenaltyRate DESC")
    List<LoanProduct> findByPenaltyRateGreaterThanOrderByRate(@Param("penaltyRate") Double penaltyRate);

    // Task 4.5: Find Loan Products With No Overdue History (LEFT JOIN + NOT EXISTS)
    @Query("SELECT lp FROM LoanProduct lp WHERE NOT EXISTS " +
           "(SELECT la FROM LoanAccount la WHERE la.loanProduct.loanCode = lp.loanCode AND la.status = 'OVERDUE')")
    List<LoanProduct> findProductsWithNoOverdueHistory();

    // Alternative: Find Loan Products With Overdue Count
    @Query("SELECT lp, COUNT(la) as overdueCount FROM LoanProduct lp " +
           "LEFT JOIN lp.loanAccounts la ON la.status = 'OVERDUE' " +
           "GROUP BY lp.loanCode HAVING COUNT(la) = 0 ORDER BY lp.loanName ASC")
    List<Object[]> findProductsWithOverdueStats();

    // ==================== TASK 5: JPQL UPDATE QUERIES ====================
    
    // Task 5.1: Increase Daily Penalty Rates for Selected Loan Categories
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = lp.dailyPenaltyRate + :increaseAmount " +
           "WHERE lp.loanType = :loanType")
    int increaseDailyPenaltyRates(@Param("loanType") String loanType, @Param("increaseAmount") Double increaseAmount);

    // Task 5.2: Increase Penalty Rate by Percentage
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = lp.dailyPenaltyRate * (1 + :percentageIncrease / 100) " +
           "WHERE lp.loanType = :loanType")
    int increasePenaltyRateByPercentage(@Param("loanType") String loanType, @Param("percentageIncrease") Double percentageIncrease);

    // Task 5.3: Increase Penalty Rate for All Products Above a Threshold
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = lp.dailyPenaltyRate + :increaseAmount " +
           "WHERE lp.dailyPenaltyRate > :threshold")
    int increasePenaltyForHighRateProducts(@Param("threshold") Double threshold, @Param("increaseAmount") Double increaseAmount);

    // Task 5.4: Batch Update for Multiple Loan Types
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = lp.dailyPenaltyRate + :increaseAmount " +
           "WHERE lp.loanType IN :loanTypes")
    int increasePenaltyForMultipleTypes(@Param("loanTypes") List<String> loanTypes, @Param("increaseAmount") Double increaseAmount);

    // Task 5.5: Increase Penalty with Cap Limit (max penalty rate)
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = LEAST(lp.dailyPenaltyRate + :increaseAmount, :maxRate) " +
           "WHERE lp.loanType = :loanType")
    int increasePenaltyWithCap(@Param("loanType") String loanType, @Param("increaseAmount") Double increaseAmount, @Param("maxRate") Double maxRate);

    // Task 5.6: Reset Penalty Rates to Standard Value
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = :standardRate WHERE lp.loanType = :loanType")
    int resetPenaltyRate(@Param("loanType") String loanType, @Param("standardRate") Double standardRate);

    // Task 5.7: Decrease Penalty Rate (if used for promotional offers)
    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct lp SET lp.dailyPenaltyRate = GREATEST(lp.dailyPenaltyRate - :decreaseAmount, 0.0) " +
           "WHERE lp.loanType = :loanType")
    int decreasePenaltyRate(@Param("loanType") String loanType, @Param("decreaseAmount") Double decreaseAmount);

    // Task 5.8: Batch Delete Loan Products (Cleanup Utility)
    @Transactional
    @Modifying
    @Query("DELETE FROM LoanProduct lp WHERE lp.dailyPenaltyRate < :minRate")
    int deleteProductsWithLowPenaltyRate(@Param("minRate") Double minRate);

    // ==================== TASK 6: PAGINATION & SORTING ====================
    
    // Task 6.1: Find all loan products with pagination (default sort by penalty rate DESC)
    @Query("SELECT lp FROM LoanProduct lp ORDER BY lp.dailyPenaltyRate DESC")
    Page<LoanProduct> findAllProductsSortedByPenaltyRate(Pageable pageable);

    // Task 6.2: Find loan products by type with pagination
    Page<LoanProduct> findByLoanType(String loanType, Pageable pageable);

    // Task 6.3: Find loan products by name with pagination
    Page<LoanProduct> findByLoanNameContainingIgnoreCase(String loanName, Pageable pageable);

    // Task 6.4: Find loan products with penalty rate greater than threshold (paginated)
    Page<LoanProduct> findByDailyPenaltyRateGreaterThan(Double dailyPenaltyRate, Pageable pageable);

    // Task 6.5: Find loan products within penalty rate range (paginated)
    Page<LoanProduct> findByDailyPenaltyRateBetween(Double minRate, Double maxRate, Pageable pageable);
}

