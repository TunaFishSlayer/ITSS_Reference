import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Get all products for customer view
export const getAllProduct = async () => {
  try {
    // This endpoint is public, no userId required for customer view
    let res = await axios.get(`${apiURL}/api/v1/products/customer`);
    return res.data;
  } catch (error) {
    console.log("Error fetching products:", error);
    throw error;
  }
};

// Get all products for manager view
export const getAllProductManager = async (userId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/manager?userId=${userId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Get product details for customer
export const getProductDetails = async (userId, productId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/customer/${productId}?userId=${userId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Get product details for manager
export const getProductDetailsManager = async (userId, productId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/manager/${productId}?userId=${userId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Search products for customer
export const searchProducts = async (userId, keyword) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/customer/search?userId=${userId}&keyword=${keyword}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Search products for manager
export const searchProductsManager = async (userId, keyword) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/manager/search?userId=${userId}&keyword=${keyword}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Get related products
export const getRelatedProducts = async (productId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/products/${productId}/related`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Create new product (Manager only)
export const createProduct = async (userId, productData) => {
  try {
    const payload = {
      category: productData.pCategory,
      name: productData.pName,
      description: productData.pDescription,
      weight: productData.pWeight || 0,
      rushEligible: productData.pRushEligible || false,
      barcode: productData.pBarcode || "",
      price: parseFloat(productData.pPrice),
      specifications: productData.pSpecifications || "",
      images: productData.pImages || []
    };

    let res = await axios.post(`${apiURL}/api/v1/products?userId=${userId}`, payload, {
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

// Update existing product (Manager only)
export const editProduct = async (userId, productId, productData) => {
  try {
    const payload = {
      category: productData.pCategory,
      name: productData.pName,
      description: productData.pDescription,
      weight: productData.pWeight || 0,
      rushEligible: productData.pRushEligible || false,
      barcode: productData.pBarcode || "",
      price: parseFloat(productData.pPrice),
      specifications: productData.pSpecifications || "",
      images: productData.pImages || []
    };

    let res = await axios.put(`${apiURL}/api/v1/products/${productId}?userId=${userId}`, payload, {
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

// Delete product (Manager only)
export const deleteProduct = async (userId, productId) => {
  try {
    let res = await axios.delete(`${apiURL}/api/v1/products/${productId}?userId=${userId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Legacy functions for backward compatibility - these may need to be updated based on actual backend implementation
export const productByCategory = async (catId) => {
  try {
    // Note: This endpoint may not exist in the current backend
    // You may need to use the search functionality instead
    console.warn("productByCategory: This endpoint may not be available in the current backend");
    let res = await axios.post(`${apiURL}/api/product/product-by-category`, {
      catId,
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const productByPrice = async (price) => {
  try {
    // Note: This endpoint may not exist in the current backend
    // You may need to use the search functionality instead
    console.warn("productByPrice: This endpoint may not be available in the current backend");
    let res = await axios.post(`${apiURL}/api/product/product-by-price`, {
      price,
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Helper function for image upload (if needed)
export const createProductImage = async ({ pImage }) => {
  /* Most important part for uploading multiple image  */
  let formData = new FormData();
  for (const file of pImage) {
    formData.append("pImage", file);
  }
  /* Most important part for uploading multiple image  */

  try {
    // Note: You may need to implement an image upload endpoint in the backend
    console.warn("createProductImage: Image upload endpoint may need to be implemented");
    let res = await axios.post(`${apiURL}/api/v1/upload/images`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
