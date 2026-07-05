package org.northernarc.loanmanagementproject.controlleradvice;

import org.northernarc.loanmanagementproject.dto.ApiResponse;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanAccountNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanProductNotFoundException;
import org.northernarc.loanmanagementproject.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle CustomerNotFoundException
     * Returns 404 Not Found response with standardized API format
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<?>> handleCustomerNotFound(
            CustomerNotFoundException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.notFound(
                ex.getMessage(),
                "CUSTOMER_NOT_FOUND"
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle LoanProductNotFoundException
     * Returns 404 Not Found response with standardized API format
     */
    @ExceptionHandler(LoanProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<?>> handleLoanProductNotFound(
            LoanProductNotFoundException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.notFound(
                ex.getMessage(),
                "LOAN_PRODUCT_NOT_FOUND"
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle LoanAccountNotFoundException
     * Returns 404 Not Found response with standardized API format
     */
    @ExceptionHandler(LoanAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<?>> handleLoanAccountNotFound(
            LoanAccountNotFoundException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.notFound(
                ex.getMessage(),
                "LOAN_ACCOUNT_NOT_FOUND"
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle ValidationException
     * Returns 400 Bad Request response with standardized API format
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            ValidationException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.badRequest(
                ex.getMessage(),
                "VALIDATION_ERROR"
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Bean Validation Errors (Spring's built-in validation)
     * Returns 400 Bad Request with field validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .statusCode(400)
                .message("Validation failed")
                .errorType("FIELD_VALIDATION_ERROR")
                .data(fieldErrors)
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Access Denied / Forbidden Errors
     * Returns 403 Forbidden response
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.forbidden(
                "Access denied: You don't have permission to access this resource"
        );
        
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle Authentication Errors
     * Returns 401 Unauthorized response
     */
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.unauthorized(
                "Authentication failed: Invalid credentials"
        );
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle Illegal Argument Exceptions
     * Returns 400 Bad Request response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.badRequest(
                ex.getMessage(),
                "INVALID_ARGUMENT"
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Generic Exceptions
     * Returns 500 Internal Server Error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        ApiResponse<?> response = ApiResponse.internalError(
                "An unexpected error occurred: " + ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
