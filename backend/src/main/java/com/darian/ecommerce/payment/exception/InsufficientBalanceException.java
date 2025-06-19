package com.darian.ecommerce.payment.exception;

public class InsufficientBalanceException extends PaymentException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
