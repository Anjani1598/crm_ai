package com.crm.statemachine.exception;

public class DealStateException extends RuntimeException {
    public DealStateException(String message) {
        super(message);
    }

    public DealStateException(String message, Throwable cause) {
        super(message, cause);
    }
} 