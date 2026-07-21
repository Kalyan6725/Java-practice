package org.northernarc.loanmanagementproject.exception;

/**
 * Thrown when a loan application cannot be located by its identifier.
 */
public class LoanApplicationNotFoundException extends RuntimeException {
    public LoanApplicationNotFoundException(String applicationId) {
        super("Loan application not found with ID: " + applicationId);
    }
}
