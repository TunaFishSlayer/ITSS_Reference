package com.darian.ecommerce.payment.exception;

public class PaymentException extends RuntimeException {
    // Base exception for payment-related errors
    public PaymentException(String message) {
    super(message);
  }
}
