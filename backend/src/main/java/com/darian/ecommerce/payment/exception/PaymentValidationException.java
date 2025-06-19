package com.darian.ecommerce.payment.exception;

import com.darian.ecommerce.shared.exception.BaseException;
import com.darian.ecommerce.shared.exception.ErrorCode;

public class PaymentValidationException extends BaseException {
    public PaymentValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}