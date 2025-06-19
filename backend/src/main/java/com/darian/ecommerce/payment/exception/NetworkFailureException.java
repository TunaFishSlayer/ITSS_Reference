package com.darian.ecommerce.payment.exception;

public class NetworkFailureException extends ConnectionException {
    public NetworkFailureException(String message) {
        super(message);
    }
}
