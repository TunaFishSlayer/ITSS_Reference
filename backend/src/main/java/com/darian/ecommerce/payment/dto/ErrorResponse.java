package com.darian.ecommerce.payment.dto;

import com.darian.ecommerce.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private int errorCode;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, ErrorCode errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode.getCode();
        this.error = errorCode.getDefaultMessage();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
