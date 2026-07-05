package org.northernarc.loanmanagementproject.exception;

public class LoanProductNotFoundException extends RuntimeException {
    public LoanProductNotFoundException(String message) {
        super("Loan Product not found with code: " + message);
    }

    public LoanProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
