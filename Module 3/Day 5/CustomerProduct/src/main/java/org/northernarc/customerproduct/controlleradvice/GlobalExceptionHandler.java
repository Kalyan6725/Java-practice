package org.northernarc.customerproduct.controlleradvice;

import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFound.class)
    public ResponseEntity<org.northernarc.jpaspringbootproject.controlleradvice.ErrorResponse> customerHandler(CustomerNotFound e){
        org.northernarc.jpaspringbootproject.controlleradvice.ErrorResponse er = new org.northernarc.jpaspringbootproject.controlleradvice.ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFound.class)
    public ResponseEntity<org.northernarc.jpaspringbootproject.controlleradvice.ErrorResponse> employeeHandler(OrderNotFound e){
        org.northernarc.jpaspringbootproject.controlleradvice.ErrorResponse er = new org.northernarc.jpaspringbootproject.controlleradvice.ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }
}