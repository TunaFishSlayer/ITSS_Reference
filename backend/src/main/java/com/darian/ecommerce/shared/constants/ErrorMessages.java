package com.darian.ecommerce.shared.constants;

public class ErrorMessages {
    // Payment related errors
    public static final String PAYMENT_INVALID_METHOD = "Invalid payment method: %s";
    public static final String PAYMENT_INVALID_AMOUNT = "Invalid payment amount for order: %s";
    public static final String PAYMENT_INVALID_STATUS = "Invalid payment status for order: %s";
    public static final String PAYMENT_NOT_FOUND = "Payment not found for order: %s";
    public static final String PAYMENT_ALREADY_PROCESSED = "Payment already processed for order: %s";
    
    // VNPay specific errors
    public static final String VNPAY_CONNECTION_ERROR = "Unable to connect to VNPay API";
    public static final String VNPAY_INVALID_RESPONSE = "Invalid response from VNPay API";
    public static final String VNPAY_TRANSACTION_FAILED = "VNPay transaction failed: %s";
    public static final String VNPAY_REFUND_FAILED = "VNPay refund failed: %s";
    
    // Order related errors
    public static final String ORDER_NOT_FOUND = "Order not found with ID: %s";
    public static final String ORDER_INVALID_STATUS = "Invalid order status: %s";
    public static final String ORDER_ALREADY_CANCELLED = "Order already cancelled: %s";
    public static final String ORDER_CANNOT_BE_MODIFIED = "Order cannot be modified in current status: %s";
    
    // Product related errors
    public static final String PRODUCT_NOT_FOUND = "Product not found with ID: %s";
    public static final String PRODUCT_NOT_AVAILABLE = "Product %s is not available";
    public static final String PRODUCT_INSUFFICIENT_STOCK = "Insufficient stock for product: %s";
    public static final String PRODUCT_INVALID_PRICE = "Invalid price for product: %s";
    
    // Cart related errors
    public static final String CART_NOT_FOUND = "Cart not found for user: %s";
    public static final String CART_ITEM_NOT_FOUND = "Item not found in cart: %s";
    public static final String CART_INVALID_QUANTITY = "Invalid quantity for product: %s";

    // Validation errors
    public static final String VALIDATION_REQUIRED_FIELD = "Required field missing: %s";
    public static final String VALIDATION_INVALID_VALUE = "Invalid value for field: %s";
    public static final String VALIDATION_FAILED = "Validation failed: %s";
    
    // Authentication/Authorization errors
    public static final String AUTH_INVALID_TOKEN = "Invalid authentication token";
    public static final String AUTH_EXPIRED_TOKEN = "Authentication token expired";
    public static final String AUTH_INSUFFICIENT_PERMISSIONS = "Insufficient permissions for this operation";

    private ErrorMessages() {
        // Private constructor to prevent instantiation
    }
} 