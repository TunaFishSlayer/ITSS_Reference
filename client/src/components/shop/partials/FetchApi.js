import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Get cart by user ID - Updated to match backend endpoint
export const getCartByUser = async (userId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/cart/${userId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Add product to cart - Updated to match backend endpoint
export const addToCart = async (userId, productData) => {
  try {
    let res = await axios.post(`${apiURL}/api/v1/cart/${userId}/add`, productData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Update cart item - Updated to match backend endpoint
export const updateCartItem = async (userId, cartData) => {
  try {
    let res = await axios.put(`${apiURL}/api/v1/cart/${userId}/update`, cartData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Remove item from cart - Updated to match backend endpoint
export const removeFromCart = async (userId, productId) => {
  try {
    let res = await axios.delete(`${apiURL}/api/v1/cart/${userId}/remove`, {
      data: { productId },
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Empty cart - Updated to match backend endpoint
export const emptyCart = async (userId) => {
  try {
    let res = await axios.delete(`${apiURL}/api/v1/cart/${userId}/empty`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Legacy function - Updated to use new cart endpoints
export const cartListProduct = async () => {
  console.warn("cartListProduct: Using new cart endpoint instead of legacy /api/product/cart-product");
  try {
    // Get current user ID from localStorage
    const jwt = localStorage.getItem("jwt");
    const userId = jwt ? JSON.parse(jwt).user?.id || JSON.parse(jwt).user?._id : null;

    if (userId) {
      // Use new cart endpoint
      let res = await axios.get(`${apiURL}/api/v1/cart/${userId}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwt ? JSON.parse(jwt).token : ""}`
        }
      });

      if (res.data && res.data.success && res.data.data) {
        // Map backend cart format to frontend expected format
        const cartItems = res.data.data.items || [];
        return {
          success: true,
          Products: cartItems.map(item => ({
            _id: item.productId,
            pName: item.productName,
            pPrice: item.productPrice,
            pImages: [item.productImageUrl || '/placeholder-product.jpg'],
            quantity: item.quantity
          }))
        };
      }
    }

    // Fallback to localStorage cart
    let carts = JSON.parse(localStorage.getItem("cart")) || [];
    return {
      success: true,
      Products: carts
    };
  } catch (error) {
    console.log("Error in cartListProduct:", error);
    // Fallback to localStorage cart
    let carts = JSON.parse(localStorage.getItem("cart")) || [];
    return {
      success: true,
      Products: carts
    };
  }
};
