package org.northernarc.jpaspringbootproject.controlleradvice;

import org.northernarc.jpaspringbootproject.exceptions.EmployeeNotFound;
import org.northernarc.jpaspringbootproject.exceptions.ProjectNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProjectNotFound.class)
    public ResponseEntity<ErrorResponse> projectHandler(ProjectNotFound e){
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeNotFound.class)
    public ResponseEntity<ErrorResponse> employeeHandler(EmployeeNotFound e){
        ErrorResponse er = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }
}