package com.darian.ecommerce.payment.exception;

public class ConnectionException extends RuntimeException {
    // Exception for connection issues with external payment systems
    public ConnectionException(String message) {
        super(message);
    }
}
