package org.northernarc.loanmanagementproject.repository;

import org.northernarc.loanmanagementproject.entity.LoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findByCustomer_CustomerId(Long customerId);

    List<LoanApplication> findByStatus(String status);

    Page<LoanApplication> findByStatus(String status, Pageable pageable);

    List<LoanApplication> findByCustomer_CustomerIdAndStatus(Long customerId, String status);

    List<LoanApplication> findByLoanProduct_LoanCode(String loanCode);

    long countByStatus(String status);

    /**
     * Underwriter work-queue: open applications (SUBMITTED or UNDER_REVIEW),
     * oldest first so the queue is processed fairly.
     */
    @Query("SELECT a FROM LoanApplication a WHERE a.status IN ('SUBMITTED','UNDER_REVIEW') " +
           "ORDER BY a.applicationDate ASC, a.applicationId ASC")
    List<LoanApplication> findOpenApplications();

    /**
     * Pending applications for a specific branch (via the applicant's branch).
     */
    @Query("SELECT a FROM LoanApplication a WHERE a.customer.branch = :branch " +
           "AND a.status IN ('SUBMITTED','UNDER_REVIEW') " +
           "ORDER BY a.applicationDate ASC")
    List<LoanApplication> findOpenApplicationsByBranch(@Param("branch") String branch);

    /**
     * Detects an already-open application for the same customer + product to
     * prevent duplicate submissions.
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM LoanApplication a " +
           "WHERE a.customer.customerId = :customerId AND a.loanProduct.loanCode = :loanCode " +
           "AND a.status IN ('SUBMITTED','UNDER_REVIEW')")
    boolean existsOpenApplication(@Param("customerId") Long customerId,
                                  @Param("loanCode") String loanCode);
}
