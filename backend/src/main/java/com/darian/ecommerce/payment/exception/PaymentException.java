package com.darian.ecommerce.payment.exception;

import com.darian.ecommerce.shared.exception.BaseException;
import com.darian.ecommerce.shared.exception.ErrorCode;

public class PaymentException extends BaseException {

    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}