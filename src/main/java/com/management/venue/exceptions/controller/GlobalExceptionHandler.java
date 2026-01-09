package com.management.venue.exceptions.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseLogger {

    // Catch our custom business exceptions
    @ExceptionHandler(StapuBoxException.class)
    public ResponseEntity<ErrorResponse> handleStapuBoxException(StapuBoxException e) {
        log.error("Business Error: Code [{}], Message: {}", e.getErrorCode(), e.getMessage());
        
        ErrorResponse response = new ErrorResponse(e.getMessage(), e.getErrorCode(),LocalDateTime.now(),null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
    }

    // Catch any other unexpected runtime exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Internal Server Error: ", e); // Logs the full stack trace
        ErrorResponse response = new ErrorResponse("An unexpected error occurred", "500",LocalDateTime.now(),e.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}