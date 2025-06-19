package com.darian.ecommerce.shared.constants;

public class Constants {
    // General constants
    public static final String SYSTEM_NAME = "Marketly E-commerce";
    public static final String API_VERSION = "v1";
    public static final String DEFAULT_TIMEZONE = "Asia/Ho_Chi_Minh";
    
    // Payment related constants
    public static final int PAYMENT_TIMEOUT_SECONDS = 300; // 5 minutes
    public static final int MAX_PAYMENT_RETRIES = 3;
    public static final long PAYMENT_RETRY_DELAY_MS = 1000; // 1 second
    
    // VNPay specific constants
    public static final String VNPAY_CURRENCY = "VND";
    public static final String VNPAY_LOCALE = "vn";
    public static final String VNPAY_VERSION = "2.1.0";
    public static final String VNPAY_API_BASE = "https://sandbox.vnpayment.vn/paymentv2";
    
    // Order related constants
    public static final int MAX_ORDER_ITEMS = 100;
    public static final int MAX_RUSH_ORDER_ITEMS = 5;
    public static final float MIN_RUSH_ORDER_VALUE = 100000;
    public static final float MAX_RUSH_ORDER_VALUE = 10000000;
    
    // Cart related constants
    public static final int MAX_CART_ITEMS = 20;
    public static final int MAX_ITEM_QUANTITY = 99;
    public static final float MIN_ORDER_VALUE = 10000;
    
    // Product related constants
    public static final int MAX_PRODUCT_NAME_LENGTH = 200;
    public static final int MAX_PRODUCT_DESCRIPTION_LENGTH = 1000;
    public static final float MIN_PRODUCT_PRICE = 1000;
    public static final float MAX_PRODUCT_PRICE = 100000000;
    
    // Audit related constants
    public static final int MAX_DELETE_LIMIT = 30;
    public static final int AUDIT_LOG_RETENTION_DAYS = 90;
    
    // Pagination defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    
    // Cache configuration
    public static final long CACHE_TTL_SECONDS = 300; // 5 minutes
    public static final int CACHE_MAX_SIZE = 1000;
    
    // Security constants
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
} 