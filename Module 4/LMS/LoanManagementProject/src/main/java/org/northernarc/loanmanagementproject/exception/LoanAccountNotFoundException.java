package org.northernarc.loanmanagementproject.exception;

public class LoanAccountNotFoundException extends RuntimeException {
    public LoanAccountNotFoundException(String message) {
        super("Loan Account not found with ID: " + message);
    }

    public LoanAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
