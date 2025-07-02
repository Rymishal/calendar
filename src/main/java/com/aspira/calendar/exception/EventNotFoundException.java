package com.aspira.calendar.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String id) {
        super("Event with id: " + id + " not found");
    }
}
