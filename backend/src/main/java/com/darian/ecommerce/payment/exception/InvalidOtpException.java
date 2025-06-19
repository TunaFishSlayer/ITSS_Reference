package com.darian.ecommerce.payment.exception;

public class InvalidOtpException extends PaymentException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
