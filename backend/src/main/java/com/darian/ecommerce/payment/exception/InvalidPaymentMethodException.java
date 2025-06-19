package com.darian.ecommerce.payment.exception;

import com.darian.ecommerce.shared.exception.ErrorCode;
import com.darian.ecommerce.shared.exception.BaseException;

public class InvalidPaymentMethodException extends BaseException {
    public InvalidPaymentMethodException(String message) {
        super( ErrorCode.INVALID_PAYMENT_METHOD, message);
    }
}
