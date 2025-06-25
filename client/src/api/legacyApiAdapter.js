/**
 * Legacy API Adapter
 * 
 * This file provides adapter functions to maintain compatibility with legacy API calls
 * while using the new backend endpoints where possible, or providing mock responses
 * where backend endpoints don't exist.
 */

import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Helper function to get auth headers
const getAuthHeaders = () => {
  const jwt = localStorage.getItem("jwt");
  const token = jwt ? JSON.parse(jwt).token : null;
  return token ? { 'Authorization': `Bearer ${token}` } : {};
};

// Helper function to get current user ID
const getCurrentUserId = () => {
  const jwt = localStorage.getItem("jwt");
  return jwt ? JSON.parse(jwt).user?.id || JSON.parse(jwt).user?._id || 1 : 1;
};

// ==================== PRODUCT LEGACY ADAPTERS ====================

/**
 * Legacy: productByCategory
 * Backend equivalent: Use search or filter functionality
 */
export const productByCategory = async (categoryName) => {
  console.warn("productByCategory: Using search functionality as alternative");
  try {
    const userId = getCurrentUserId();
    const res = await axios.get(
      `${apiURL}/api/v1/products/customer/search?userId=${userId}&keyword=${categoryName}`,
      { headers: getAuthHeaders() }
    );
    return res.data;
  } catch (error) {
    console.log("Error in productByCategory adapter:", error);
    return [];
  }
};

/**
 * Legacy: productByPrice
 * Backend equivalent: Use search with price filter (not implemented in current backend)
 */
export const productByPrice = async (priceRange) => {
  console.warn("productByPrice: Backend does not support price filtering. Returning all products.");
  try {
    const res = await axios.get(`${apiURL}/api/v1/products/customer`);
    // Client-side price filtering as fallback
    const products = res.data || [];
    if (priceRange && priceRange.min !== undefined && priceRange.max !== undefined) {
      return products.filter(product => 
        product.price >= priceRange.min && product.price <= priceRange.max
      );
    }
    return products;
  } catch (error) {
    console.log("Error in productByPrice adapter:", error);
    return [];
  }
};

// ==================== CART LEGACY ADAPTERS ====================

/**
 * Legacy: cartListProduct
 * Backend equivalent: Use cart endpoints
 */
export const cartListProduct = async () => {
  console.warn("cartListProduct: Using new cart endpoint");
  try {
    const userId = getCurrentUserId();
    const res = await axios.get(`${apiURL}/api/v1/cart/${userId}`, {
      headers: getAuthHeaders()
    });
    return res.data;
  } catch (error) {
    console.log("Error in cartListProduct adapter:", error);
    // Fallback to localStorage cart
    const localCart = JSON.parse(localStorage.getItem("cart")) || [];
    return { cartItems: localCart };
  }
};

// ==================== WISHLIST LEGACY ADAPTERS ====================

/**
 * Legacy: wishListProducts
 * Backend equivalent: Get products by IDs (no dedicated wishlist endpoint)
 */
export const wishListProducts = async () => {
  console.warn("wishListProducts: Using product details endpoints for wishlist items");
  try {
    const wishList = JSON.parse(localStorage.getItem("wishList")) || [];
    if (wishList.length === 0) return [];

    const userId = getCurrentUserId();
    const productPromises = wishList.map(productId =>
      axios.get(`${apiURL}/api/v1/products/customer/${productId}?userId=${userId}`)
        .catch(error => {
          console.log(`Error fetching product ${productId}:`, error);
          return null;
        })
    );

    const responses = await Promise.all(productPromises);
    return responses
      .filter(response => response !== null)
      .map(response => response.data);
  } catch (error) {
    console.log("Error in wishListProducts adapter:", error);
    return [];
  }
};

// ==================== ORDER LEGACY ADAPTERS ====================

/**
 * Legacy: getOrderByUser
 * Backend equivalent: /api/v1/orders/history/{customerId}
 */
export const getOrderByUser = async (userId) => {
  console.warn("getOrderByUser: Using order history endpoint");
  try {
    const res = await axios.get(`${apiURL}/api/v1/orders/history/${userId}`, {
      headers: getAuthHeaders()
    });
    return { success: true, Order: res.data };
  } catch (error) {
    console.log("Error in getOrderByUser adapter:", error);
    return { success: true, Order: [] };
  }
};

/**
 * Legacy: updateOrder
 * Backend equivalent: Not available (orders are immutable in current backend)
 */
export const updateOrder = async (orderId, updateData) => {
  console.warn("updateOrder: Order updates not supported in current backend");
  return {
    success: false,
    message: "Order updates are not supported in the current backend implementation"
  };
};

// ==================== BRAINTREE LEGACY ADAPTERS ====================

/**
 * Legacy: getBrainTreeToken
 * Backend equivalent: Not available (VNPay is used instead)
 */
export const getBrainTreeToken = async () => {
  console.warn("getBrainTreeToken: Braintree not supported. Use VNPay instead.");
  return {
    success: false,
    message: "Braintree payment is not supported. Please use VNPay payment method."
  };
};

/**
 * Legacy: getPaymentProcess
 * Backend equivalent: VNPay payment endpoints
 */
export const getPaymentProcess = async (paymentData) => {
  console.warn("getPaymentProcess: Redirecting to VNPay payment");
  try {
    const { orderId } = paymentData;
    const res = await axios.post(
      `${apiURL}/api/v1/payment/${orderId}`,
      { paymentMethod: "VNPAY" },
      { headers: getAuthHeaders() }
    );
    return res.data;
  } catch (error) {
    console.log("Error in getPaymentProcess adapter:", error);
    return {
      success: false,
      message: "Payment processing failed"
    };
  }
};

// ==================== USER LEGACY ADAPTERS ====================

/**
 * Legacy: getUserById
 * Backend equivalent: Not available (return data from JWT)
 */
export const getUserById = async (userId) => {
  console.warn("getUserById: Using JWT data as user information source");
  try {
    const jwt = localStorage.getItem("jwt");
    if (jwt) {
      const userData = JSON.parse(jwt).user;
      return {
        success: true,
        User: {
          _id: userData.id || userData._id,
          name: userData.username || userData.name || "User",
          email: userData.email || "user@example.com",
          phoneNumber: userData.phoneNumber || "N/A",
          role: userData.role || 0
        }
      };
    }
    return { success: false, message: "User not found" };
  } catch (error) {
    console.log("Error in getUserById adapter:", error);
    return { success: false, message: "Error retrieving user data" };
  }
};

// ==================== CATEGORY LEGACY ADAPTERS ====================

/**
 * Legacy: getAllCategory
 * Backend equivalent: Not available (return hardcoded categories from data-init.sql)
 */
export const getAllCategory = async () => {
  console.warn("getAllCategory: Using hardcoded categories from database schema");
  return {
    success: true,
    Categories: [
      { _id: 1, cName: "Electronics", cDescription: "Electronic devices and gadgets", cStatus: "Active" },
      { _id: 2, cName: "Fashion and Apparel", cDescription: "Clothing and accessories", cStatus: "Active" },
      { _id: 3, cName: "Beauty and Personal Care", cDescription: "Cosmetics and personal care products", cStatus: "Active" },
      { _id: 4, cName: "Furniture", cDescription: "Home and office furniture", cStatus: "Active" },
      { _id: 5, cName: "Beverages", cDescription: "Drinks and beverages", cStatus: "Active" },
      { _id: 6, cName: "Food", cDescription: "Food items and snacks", cStatus: "Active" },
      { _id: 7, cName: "Household Essentials", cDescription: "Household cleaning and essentials", cStatus: "Active" },
      { _id: 8, cName: "Toys and Hobbies", cDescription: "Toys and hobby-related products", cStatus: "Active" },
      { _id: 9, cName: "Media", cDescription: "Books, movies, and music", cStatus: "Active" }
    ]
  };
};

// Export all adapters
export default {
  product: {
    productByCategory,
    productByPrice
  },
  cart: {
    cartListProduct
  },
  wishlist: {
    wishListProducts
  },
  order: {
    getOrderByUser,
    updateOrder
  },
  payment: {
    getBrainTreeToken,
    getPaymentProcess
  },
  user: {
    getUserById
  },
  category: {
    getAllCategory
  }
};
