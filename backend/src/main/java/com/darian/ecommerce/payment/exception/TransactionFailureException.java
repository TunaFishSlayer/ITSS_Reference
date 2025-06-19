package com.darian.ecommerce.payment.exception;

public class TransactionFailureException extends PaymentException {
    public TransactionFailureException(String message) {
        super(message);
    }
}
