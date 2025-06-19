package com.darian.ecommerce.payment.exception;

public class PaymentFailedException extends PaymentProcessingException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
