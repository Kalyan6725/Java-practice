package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    // Task 3: Derived Query Methods with eager loading
    @Query("SELECT DISTINCT a FROM Account a JOIN FETCH a.customer WHERE a.accountType = :accountType")
    List<Account> findByAccountType(@Param("accountType") String accountType);
    
    @Query("SELECT DISTINCT a FROM Account a JOIN FETCH a.customer WHERE a.balance > :amount")
    List<Account> findByBalanceGreaterThan(@Param("amount") double amount);
    
    long countByCustomerCustomerId(Long customerId);

    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a WHERE a.customer.customerId = :customerId")
    Double sumBalanceByCustomerId(@Param("customerId") Long customerId);

    // Task 4: JPQL Custom Query with DISTINCT and LEFT JOIN FETCH
    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.transactions t WHERE t IS NULL")
    List<Account> findAccountsWithNoTransactions();

    // Task 5: JPQL Update Query
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    int increaseBalance(@Param("accountNumber") String accountNumber, @Param("amount") double amount);

    // Final Challenge: Aggregated dashboard metric
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a")
    Double findTotalBalance();

    // Final Challenge: Single optimized dashboard query
    @Query(value = """
            SELECT
                (SELECT COUNT(*) FROM customers) AS total_customers,
                (SELECT COUNT(*) FROM accounts) AS total_accounts,
                COALESCE((SELECT SUM(a.balance) FROM accounts a), 0) AS total_balance,
                (
                    SELECT c.branch
                    FROM customers c
                    JOIN accounts a ON a.customer_id = c.customer_id
                    GROUP BY c.branch
                    ORDER BY SUM(a.balance) DESC
                    LIMIT 1
                ) AS top_branch,
                (
                    SELECT c.customer_name
                    FROM customers c
                    JOIN accounts a ON a.customer_id = c.customer_id
                    GROUP BY c.customer_id, c.customer_name
                    ORDER BY SUM(a.balance) DESC
                    LIMIT 1
                ) AS highest_balance_customer
            """, nativeQuery = true)
    List<Object[]> fetchDashboardMetrics();

}
