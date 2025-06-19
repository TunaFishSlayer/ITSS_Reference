package com.darian.ecommerce.payment.exception;

public class PaymentProcessingException extends RuntimeException {
    // Exception for errors during payment/refund processing
    public PaymentProcessingException(String message) {
        super(message);
    }
}
