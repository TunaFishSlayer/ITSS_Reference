package com.darian.ecommerce.payment.exception;

public class RefundFailedException extends PaymentProcessingException {
    public RefundFailedException(String message) {
        super(message);
    }
}
