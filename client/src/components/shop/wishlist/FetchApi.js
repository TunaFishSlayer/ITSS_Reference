import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Get wishlist products - Legacy function (may need backend implementation)
export const wishListProducts = async () => {
  console.warn("wishListProducts: This endpoint may not be available in the current backend");
  let productArray = JSON.parse(localStorage.getItem("wishList"));
  try {
    let res = await axios.post(`${apiURL}/api/product/wish-product`, {
      productArray,
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Alternative: Get multiple products by IDs using the existing product endpoint
export const getProductsByIds = async (userId, productIds) => {
  try {
    // Since there's no specific wishlist endpoint, we can get products individually
    // or implement a batch get endpoint in the backend
    const promises = productIds.map(productId =>
      axios.get(`${apiURL}/api/v1/products/customer/${productId}?userId=${userId}`)
    );

    const responses = await Promise.all(promises);
    return responses.map(res => res.data);
  } catch (error) {
    console.log(error);
    throw error;
  }
};
