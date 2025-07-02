package com.aspira.calendar.exception;

public class ForbiddenEventModificationException extends RuntimeException {
    public ForbiddenEventModificationException(String message) {
        super(message);
    }
}
