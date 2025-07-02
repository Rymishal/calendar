package com.aspira.calendar.controllers;

import com.aspira.calendar.exception.EventNotFoundException;
import com.aspira.calendar.exception.ForbiddenEventModificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler({EventNotFoundException.class})
    public ResponseEntity<String> handleValidationException(EventNotFoundException eventNotFoundException) {
        return ResponseEntity.status(404).body(eventNotFoundException.getMessage());
    }

    @ExceptionHandler({ForbiddenEventModificationException.class})
    public ResponseEntity<String> handleValidationException(ForbiddenEventModificationException forbiddenEventModificationException) {
        return ResponseEntity.status(403).body(forbiddenEventModificationException.getMessage());
    }
}
