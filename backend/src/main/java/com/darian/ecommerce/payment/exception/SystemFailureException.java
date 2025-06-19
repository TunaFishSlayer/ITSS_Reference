package com.darian.ecommerce.payment.exception;

public class SystemFailureException extends ConnectionException {
    public SystemFailureException(String message) {
        super(message);
    }
}
