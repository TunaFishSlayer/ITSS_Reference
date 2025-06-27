package com.darian.ecommerce.payment.exception;

import com.darian.ecommerce.shared.exception.ErrorCode;
import com.darian.ecommerce.shared.exception.BaseException;

public class InvalidPaymentMethodException extends BaseException {
    public InvalidPaymentMethodException(String paymentMethod) {
        super(
                ErrorCode.INVALID_PAYMENT_METHOD,
                ErrorCode.INVALID_PAYMENT_METHOD.format(paymentMethod)
        );
    }
}
