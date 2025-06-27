import { apiClient, API_ENDPOINTS, handleApiError, getCurrentUserId } from './apiConfig';

// ==================== AUTH SERVICES ====================
export const authService = {
  login: async (credentials) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.AUTH_LOGIN, credentials);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  register: async (userData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.AUTH_REGISTER, userData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  logout: () => {
    localStorage.removeItem("jwt");
    localStorage.removeItem("cart");
    localStorage.removeItem("wishList");
    window.location.href = "/";
  }
};

// ==================== PRODUCT SERVICES ====================
export const productService = {
  // Get all products for customer
  getAllCustomer: async () => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.PRODUCTS_CUSTOMER);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Get all products for manager
  getAllManager: async (userId) => {
    try {
      const response = await apiClient.get(`${API_ENDPOINTS.PRODUCTS_MANAGER}?userId=${userId}`);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Get product details for customer
  getDetailsCustomer: async (userId, productId) => {
    try {
      const response = await apiClient.get(`${API_ENDPOINTS.PRODUCT_CUSTOMER_DETAILS(productId)}?userId=${userId}`);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Get product details for manager
  getDetailsManager: async (userId, productId) => {
    try {
      const response = await apiClient.get(`${API_ENDPOINTS.PRODUCT_MANAGER_DETAILS(productId)}?userId=${userId}`);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Search products for customer
  searchCustomer: async (userId, keyword) => {
    try {
      const response = await apiClient.get(`${API_ENDPOINTS.PRODUCT_CUSTOMER_SEARCH}?userId=${userId}&keyword=${keyword}`);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Search products for manager
  searchManager: async (userId, keyword) => {
    try {
      const response = await apiClient.get(`${API_ENDPOINTS.PRODUCT_MANAGER_SEARCH}?userId=${userId}&keyword=${keyword}`);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Get related products
  getRelated: async (productId) => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.PRODUCT_RELATED(productId));
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Create new product (Manager only)
  create: async (userId, productData) => {
    try {
      const response = await apiClient.post(`${API_ENDPOINTS.PRODUCTS}?userId=${userId}`, productData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Update product (Manager only)
  update: async (userId, productId, productData) => {
    try {
      const response = await apiClient.put(`${API_ENDPOINTS.PRODUCT_BY_ID(productId)}?userId=${userId}`, productData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Delete product (Manager only)
  delete: async (userId, productId) => {
    try {
      const response = await apiClient.delete(`${API_ENDPOINTS.PRODUCT_BY_ID(productId)}?userId=${userId}`);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  }
};

// ==================== CART SERVICES ====================
export const cartService = {
  // Get cart by user
  getByUser: async (userId) => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.CART_BY_USER(userId));
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Add item to cart
  addItem: async (userId, productData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.CART_ADD(userId), productData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Update cart item
  updateItem: async (userId, cartData) => {
    try {
      const response = await apiClient.put(API_ENDPOINTS.CART_UPDATE(userId), cartData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Remove item from cart
  removeItem: async (userId, productId) => {
    try {
      const response = await apiClient.delete(API_ENDPOINTS.CART_REMOVE(userId), {
        data: { productId }
      });
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Empty cart
  empty: async (userId) => {
    try {
      const response = await apiClient.delete(API_ENDPOINTS.CART_EMPTY(userId));
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  }
};

// ==================== ORDER SERVICES ====================
export const orderService = {
  // Get all orders
  getAll: async () => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.ORDERS);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Get order by ID
  getById: async (orderId) => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.ORDER_BY_ID(orderId));
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Create order
  create: async (orderData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ORDERS, orderData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Place order
  place: async (orderData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ORDER_PLACE, orderData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Cancel order
  cancel: async (orderId) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ORDER_CANCEL(orderId));
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Pay for order
  pay: async (orderId, paymentData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ORDER_PAYMENT(orderId), paymentData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  }
};

// ==================== PAYMENT SERVICES ====================
export const paymentService = {
  // Create payment
  create: async (orderId, paymentData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.PAYMENT_BY_ORDER(orderId), paymentData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  },

  // Refund payment
  refund: async (orderId, refundData) => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.PAYMENT_REFUND(orderId), refundData);
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error);
    }
  }
};

// Export all services
export default {
  auth: authService,
  product: productService,
  cart: cartService,
  order: orderService,
  payment: paymentService
};
