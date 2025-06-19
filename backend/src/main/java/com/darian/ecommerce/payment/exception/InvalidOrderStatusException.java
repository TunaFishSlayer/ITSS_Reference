package com.darian.ecommerce.payment.exception;

public class InvalidOrderStatusException extends PaymentException {
  public InvalidOrderStatusException(String message) {
    super(message);
  }
}
