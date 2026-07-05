package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Task 3: Derived Query Method with eager loading
    @Query("SELECT DISTINCT c FROM Customer c JOIN FETCH c.accounts WHERE c.branch = :branch")
    List<Customer> findByBranch(@Param("branch") String branch);

    // Task 4: JPQL Custom Queries
    @Query("""
            SELECT DISTINCT c
            FROM Customer c
            JOIN FETCH c.accounts a
            WHERE EXISTS (
                SELECT 1
                FROM Account a2
                WHERE a2.customer = c
                  AND a2.balance > :threshold
            )
            """)
    List<Customer> findRichCustomers(@Param("threshold") double threshold);

    @Query("SELECT c.branch, SUM(a.balance) FROM Customer c JOIN c.accounts a GROUP BY c.branch")
    List<Object[]> findTotalBalancePerBranch();

    @Query("""
            SELECT DISTINCT c
            FROM Customer c
            JOIN FETCH c.accounts a
            WHERE c.customerId IN (
                SELECT c2.customerId
                FROM Customer c2
                JOIN c2.accounts a2
                GROUP BY c2.customerId
                HAVING COUNT(a2) > 1
            )
            """)
    List<Customer> findCustomersWithMultipleAccounts();

    // Final Challenge: Top-performing branch by total balance
    @Query("SELECT c.branch FROM Customer c JOIN c.accounts a GROUP BY c.branch ORDER BY SUM(a.balance) DESC")
    List<String> findTopBranchByTotalBalance(Pageable pageable);

    // Final Challenge: Customer with highest cumulative balance
    @Query("SELECT c.customerName FROM Customer c JOIN c.accounts a GROUP BY c.customerId, c.customerName ORDER BY SUM(a.balance) DESC")
    List<String> findHighestBalanceCustomerName(Pageable pageable);

    // Security Helper
    Optional<Customer> findByEmail(String email);
}
