package org.northernarc.customerproduct.controlleradvice;

import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ErrorResponse er = new ErrorResponse(message.isBlank() ? "Validation failed" : message);
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericHandler(Exception e) {
        ErrorResponse er = new ErrorResponse("Something went wrong");
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}