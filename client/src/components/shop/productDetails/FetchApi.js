import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Get single product details - Updated to match backend endpoint
export const getSingleProduct = async (productId, userId = null) => {
  try {
    // Get userId from localStorage if not provided
    if (!userId) {
      const jwt = localStorage.getItem("jwt");
      userId = jwt
        ? JSON.parse(jwt).user?.id || JSON.parse(jwt).user?._id || 1
        : 1;
    }

    let res = await axios.get(
      `${apiURL}/api/v1/products/customer/${productId}?userId=${userId}`
    );
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Get related products - Updated to match backend endpoint
export const getRelatedProducts = async (productId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/${productId}/related`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Legacy functions - these may need to be updated based on actual backend implementation
export const postAddReview = async (formData) => {
  console.warn(
    "postAddReview: This endpoint may not be available in the current backend"
  );
  try {
    let res = await axios.post(`${apiURL}/api/product/add-review`, formData);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const postDeleteReview = async (formData) => {
  console.warn(
    "postDeleteReview: This endpoint may not be available in the current backend"
  );
  try {
    let res = await axios.post(`${apiURL}/api/product/delete-review`, formData);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
