package com.darian.ecommerce.payment.exception;

public class TransactionTimeoutException extends PaymentException {
    public TransactionTimeoutException(String message) {
        super(message);
    }
}
