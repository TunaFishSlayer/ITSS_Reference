package com.darian.ecommerce.payment.exception;

public class CardNotRegisteredException extends PaymentException {
    public CardNotRegisteredException(String message) {
        super(message);
    }
}
