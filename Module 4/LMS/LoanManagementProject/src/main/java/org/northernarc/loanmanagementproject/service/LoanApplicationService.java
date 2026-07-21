package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.dto.request.LoanApplicationRequest;
import org.northernarc.loanmanagementproject.dto.request.LoanApplicationReviewRequest;
import org.northernarc.loanmanagementproject.entity.LoanApplication;

import java.util.List;

/**
 * LoanApplicationService - Core loan workflow: submit, track and underwrite
 * applications (SUBMITTED -> UNDER_REVIEW -> APPROVED/REJECTED). Approval
 * creates the corresponding LoanAccount.
 */
public interface LoanApplicationService {

    LoanApplication apply(LoanApplicationRequest request, Long applicantCustomerId);

    LoanApplication getById(Long applicationId);

    List<LoanApplication> getByCustomer(Long customerId);

    List<LoanApplication> getAll();

    List<LoanApplication> getByStatus(String status);

    /**
     * Underwriter work-queue: SUBMITTED + UNDER_REVIEW, oldest first.
     */
    List<LoanApplication> getOpenQueue();

    /**
     * Apply an underwriter decision. On APPROVED a LoanAccount is created and
     * linked; on REJECTED the application is closed with remarks.
     */
    LoanApplication review(Long applicationId, LoanApplicationReviewRequest request);
}
