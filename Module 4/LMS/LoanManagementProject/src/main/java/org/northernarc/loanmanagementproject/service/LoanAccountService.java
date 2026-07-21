package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.LoanApplication;

import java.util.List;

/**
 * LoanAccountService - Sanctioned loan lifecycle. Accounts are created from an
 * approved application and then disbursed to become ACTIVE.
 */
public interface LoanAccountService {

    /**
     * Create a sanctioned loan account from an approved application.
     * @param approvedAmount optional override of the requested amount.
     */
    LoanAccount createFromApplication(LoanApplication application, Double approvedAmount);

    /**
     * Disburse an approved account: sets disbursement/start dates, first EMI due
     * date and moves the account to ACTIVE.
     */
    LoanAccount disburse(Long loanAccountId);

    LoanAccount getById(Long loanAccountId);

    List<LoanAccount> getAll();

    List<LoanAccount> getByCustomerId(Long customerId);

    List<LoanAccount> getByStatus(String status);
}
