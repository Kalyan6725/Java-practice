package org.northernarc.customerproduct.controlleradvice;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.northernarc.customerproduct.exceptions.UsernameAlreadyExistsException;
import org.northernarc.customerproduct.exceptions.ValidationFailedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFound.class)
    public ResponseEntity<ErrorResponse> customerHandler(CustomerNotFound e){
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFound.class)
    public ResponseEntity<ErrorResponse> orderHandler(OrderNotFound e){
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<ErrorResponse> productHandler(ProductNotFound e) {
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> emptyResultHandler(EmptyResultDataAccessException e) {
        ErrorResponse er = new ErrorResponse("Requested resource not found");
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return validationFailedHandler(
                new ValidationFailedException(message.isBlank() ? "Validation failed" : message)
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> beanValidationHandler(ValidationException e) {
        if (e instanceof ConstraintViolationException constraintViolationException) {
            String message = constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));

            return validationFailedHandler(
                    new ValidationFailedException(message.isBlank() ? "Validation failed" : message)
            );
        }

        String message = e.getMessage() == null || e.getMessage().isBlank()
                ? "Validation failed"
                : e.getMessage();
        return validationFailedHandler(new ValidationFailedException(message));
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ErrorResponse> validationFailedHandler(ValidationFailedException e) {
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedHandler(AccessDeniedException e) {
        ErrorResponse er = new ErrorResponse("Access forbidden: You don't have permission to access this resource");
        return new ResponseEntity<>(er, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> usernameExistsHandler(UsernameAlreadyExistsException e) {
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericHandler(Exception e) {
        ErrorResponse er = new ErrorResponse("Something went wrong");
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}