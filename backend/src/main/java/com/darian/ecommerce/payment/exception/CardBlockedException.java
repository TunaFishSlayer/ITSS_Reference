package com.darian.ecommerce.payment.exception;

public class CardBlockedException extends PaymentException {
    public CardBlockedException(String message) {
        super(message);
    }
}
