import axios from "axios";

// API Configuration
export const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

// API Endpoints Constants (matching backend ApiEndpoints.java)
export const API_ENDPOINTS = {
  // Base URLs
  BASE_API: "/api/v1",
  
  // Auth endpoints
  AUTH: "/api/v1/auth",
  AUTH_LOGIN: "/api/v1/auth/login",
  AUTH_REGISTER: "/api/v1/auth/register",
  
  // Cart endpoints
  CART: "/api/v1/cart",
  CART_BY_USER: (userId) => `/api/v1/cart/${userId}`,
  CART_ADD: (userId) => `/api/v1/cart/${userId}/add`,
  CART_UPDATE: (userId) => `/api/v1/cart/${userId}/update`,
  CART_REMOVE: (userId) => `/api/v1/cart/${userId}/remove`,
  CART_EMPTY: (userId) => `/api/v1/cart/${userId}/empty`,
  
  // Product endpoints
  PRODUCTS: "/api/v1/products",
  PRODUCTS_CUSTOMER: "/api/v1/products/customer",
  PRODUCTS_MANAGER: "/api/v1/products/manager",
  PRODUCT_BY_ID: (productId) => `/api/v1/products/${productId}`,
  PRODUCT_CUSTOMER_DETAILS: (productId) => `/api/v1/products/customer/${productId}`,
  PRODUCT_MANAGER_DETAILS: (productId) => `/api/v1/products/manager/${productId}`,
  PRODUCT_CUSTOMER_SEARCH: "/api/v1/products/customer/search",
  PRODUCT_MANAGER_SEARCH: "/api/v1/products/manager/search",
  PRODUCT_RELATED: (productId) => `/api/v1/products/${productId}/related`,
  
  // Order endpoints
  ORDERS: "/api/v1/orders",
  ORDER_BY_ID: (orderId) => `/api/v1/orders/${orderId}`,
  ORDER_PLACE: "/api/v1/orders/place",
  ORDER_PAYMENT: (orderId) => `/api/v1/orders/${orderId}/pay`,
  ORDER_CANCEL: (orderId) => `/api/v1/orders/${orderId}/cancel`,
  ORDER_DELIVERY: (orderId) => `/api/v1/orders/${orderId}/delivery`,
  ORDER_RUSH_DELIVERY: (orderId) => `/api/v1/orders/${orderId}/rush-delivery`,
  
  // Payment endpoints
  PAYMENT: "/api/v1/payment",
  PAYMENT_BY_ORDER: (orderId) => `/api/v1/payment/${orderId}`,
  PAYMENT_REFUND: (orderId) => `/api/v1/payment/${orderId}/refund`,
};

// Create axios instance with default config
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 seconds timeout
});

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("jwt") 
      ? JSON.parse(localStorage.getItem("jwt")).token 
      : null;
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      // Unauthorized - clear token and redirect to login
      localStorage.removeItem("jwt");
      localStorage.removeItem("cart");
      localStorage.removeItem("wishList");
      window.location.href = "/login";
    }
    
    return Promise.reject(error);
  }
);

// Helper functions for common operations
export const getAuthHeaders = () => {
  const token = localStorage.getItem("jwt") 
    ? JSON.parse(localStorage.getItem("jwt")).token 
    : null;
  
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const getCurrentUserId = () => {
  const jwt = localStorage.getItem("jwt");
  return jwt ? JSON.parse(jwt).user?.id || JSON.parse(jwt).user?._id : null;
};

export const isAuthenticated = () => {
  return !!localStorage.getItem("jwt");
};

export const isAdmin = () => {
  const jwt = localStorage.getItem("jwt");
  return jwt ? JSON.parse(jwt).user?.role === 1 : false;
};

// API Error handler
export const handleApiError = (error) => {
  console.error("API Error:", error);
  
  if (error.response) {
    // Server responded with error status
    const { status, data } = error.response;
    return {
      success: false,
      message: data?.message || `Server error: ${status}`,
      status,
      data: data
    };
  } else if (error.request) {
    // Request was made but no response received
    return {
      success: false,
      message: "Network error: Unable to connect to server",
      status: 0
    };
  } else {
    // Something else happened
    return {
      success: false,
      message: error.message || "An unexpected error occurred",
      status: 0
    };
  }
};

export default apiClient;
