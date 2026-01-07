package com.management.venue.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.pojo.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StapuBoxException.class)
    public ResponseEntity<ErrorResponse> handleStapuBoxException(StapuBoxException ex) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            ex.getErrorCode(),
            LocalDateTime.now(),
            // Optionally get the name of the wrapped cause
            ex.getSpecificCause() != null ? ex.getSpecificCause().getClass().getSimpleName() : "No sub-cause"
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        
        ErrorResponse error = new ErrorResponse(
            "An unexpected error occurred",
            "INTERNAL_SERVER_ERROR",
            LocalDateTime.now(),
            ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}