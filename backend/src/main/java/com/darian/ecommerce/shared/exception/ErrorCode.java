package com.darian.ecommerce.shared.exception;

public enum ErrorCode {
    // 1xxx - Authentication errors
    INVALID_CREDENTIALS(1001, "Invalid username or password"),
    USER_NOT_FOUND(1002, "User not found"),
    USERNAME_ALREADY_EXISTS(1003, "Username already exists"),
    EMAIL_ALREADY_EXISTS(1004, "Email already exists"),
    INVALID_TOKEN(1005, "Invalid authentication token"),
    INSUFFICIENT_PERMISSIONS(1006, "Insufficient permissions for this operation"),
    TOKEN_EXPIRED(1007, "Authentication token expired"),

    // 2xxx - Validation errors
    VALIDATION_ERROR(2001, "Validation failed: %s"),
    INVALID_REQUEST_DATA(2002, "Invalid request data"),
    MISSING_REQUIRED_FIELD(2003, "Required field is missing: %s"),
    INVALID_FORMAT(2004, "Invalid data format: %s"),
    INVALID_PAYMENT_METHOD(2005, "Invalid payment method: %s"),
    INVALID_FIELD_VALUE(2006, "Invalid value for field: %s"),


    // 3xxx - Resource errors
    RESOURCE_NOT_FOUND(3001, "Resource not found"),
    ORDER_NOT_FOUND(3002, "Order not found with ID: %s"),
    PRODUCT_NOT_FOUND(3003, "Product not found with ID: %s"),
    CART_NOT_FOUND(3004, "Cart not found for user: %s"),
    CART_ITEM_NOT_FOUND(3005, "Item not found in cart: %s"),

    // 4xxx - Business logic errors
    INSUFFICIENT_STOCK(4001, "Insufficient stock for product: %s"),
    ORDER_ALREADY_CANCELLED(4002, "Order already cancelled: %s"),
    INVALID_ORDER_STATUS(4003, "Invalid order status: %s"),
    PAYMENT_FAILED(4004, "Payment failed for order: %s"),
    DELETE_LIMIT_EXCEEDED(4005, "Delete limit exceeded"),
    INVALID_PAYMENT_AMOUNT(4006, "Invalid payment amount for order: %s"),
    INVALID_PAYMENT_STATUS(4007, "Invalid payment status for order: %s"),
    PAYMENT_NOT_FOUND(4008, "Payment not found for order: %s"),
    PAYMENT_ALREADY_PROCESSED(4009, "Payment already processed for order: %s"),
    ORDER_CANNOT_BE_MODIFIED(4010, "Order cannot be modified in current status: %s"),
    PRODUCT_NOT_AVAILABLE(4011, "Product %s is not available"),
    INVALID_PRODUCT_PRICE(4012, "Invalid price for product: %s"),
    CART_INVALID_QUANTITY(4013, "Invalid quantity for product: %s"),

    // 5xxx - Server & external errors
    INTERNAL_SERVER_ERROR(5001, "Internal server error"),
    DATABASE_ERROR(5002, "Database operation failed"),
    EXTERNAL_SERVICE_ERROR(5003, "External service error"),
    VNPAY_CONNECTION_ERROR(5004, "Unable to connect to VNPay API"),
    VNPAY_INVALID_RESPONSE(5005, "Invalid response from VNPay API"),
    VNPAY_TRANSACTION_FAILED(5006, "VNPay transaction failed: %s"),
    VNPAY_REFUND_FAILED(5007, "VNPay refund failed: %s");

    private final int code;
    private final String messageTemplate;

    ErrorCode(int code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public int getCode() {
        return code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public String format(Object... args) {
        return String.format(messageTemplate, args);
    }
} 