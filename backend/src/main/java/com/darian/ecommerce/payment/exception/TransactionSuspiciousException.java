package com.darian.ecommerce.payment.exception;

public class TransactionSuspiciousException extends PaymentException {
    public TransactionSuspiciousException(String message) {
        super(message);
    }
}
