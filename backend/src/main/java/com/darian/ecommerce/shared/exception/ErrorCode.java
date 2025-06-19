package com.darian.ecommerce.shared.exception;

public enum ErrorCode {
    // Authentication errors (1xxx)
    INVALID_CREDENTIALS(1001, "Invalid username or password"),
    USER_NOT_FOUND(1002, "User not found"),
    USERNAME_ALREADY_EXISTS(1003, "Username already exists"),
    EMAIL_ALREADY_EXISTS(1004, "Email already exists"),
    INVALID_TOKEN(1005, "Invalid or expired token"),
    INSUFFICIENT_PERMISSIONS(1006, "Insufficient permissions for this operation"),

    // Validation errors (2xxx)
    VALIDATION_ERROR(2001, "Validation failed"),
    INVALID_REQUEST_DATA(2002, "Invalid request data"),

    MISSING_REQUIRED_FIELD(2003, "Required field is missing"),
    INVALID_FORMAT(2004, "Invalid data format"),
    INVALID_PAYMENT_METHOD(2005, "Invalid payment method"),


    // Resource errors (3xxx)
    RESOURCE_NOT_FOUND(3001, "Resource not found"),
    ORDER_NOT_FOUND(3002, "Order not found"),
    PRODUCT_NOT_FOUND(3003, "Product not found"),
    CART_NOT_FOUND(3004, "Cart not found"),

    // Business logic errors (4xxx)
    INSUFFICIENT_STOCK(4001, "Insufficient stock"),
    ORDER_ALREADY_CANCELLED(4002, "Order is already cancelled"),
    INVALID_ORDER_STATUS(4003, "Invalid order status"),
    PAYMENT_FAILED(4004, "Payment processing failed"),
    DELETE_LIMIT_EXCEEDED(4005, "Delete limit exceeded"),

    // Server errors (5xxx)
    INTERNAL_SERVER_ERROR(5001, "Internal server error"),
    DATABASE_ERROR(5002, "Database operation failed"),
    EXTERNAL_SERVICE_ERROR(5003, "External service error");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
} 