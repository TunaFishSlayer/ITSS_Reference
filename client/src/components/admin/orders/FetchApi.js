import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Get all orders - Updated to match backend endpoint
export const getAllOrder = async () => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/orders`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Create new order - Updated to match backend endpoint
export const createOrder = async (orderData) => {
  try {
    let res = await axios.post(`${apiURL}/api/v1/orders`, orderData, {
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

// Place order - Updated to match backend endpoint
export const placeOrder = async (orderData) => {
  try {
    let res = await axios.post(`${apiURL}/api/v1/orders/place`, orderData, {
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

// Cancel order - Updated to match backend endpoint
export const cancelOrder = async (orderId) => {
  try {
    let res = await axios.post(`${apiURL}/api/v1/orders/${orderId}/cancel`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Get order by ID - Updated to match backend endpoint
export const getOrderById = async (orderId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/orders/${orderId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Legacy functions - these may need to be updated based on actual backend implementation
export const editCategory = async (oId, status) => {
  console.warn("editCategory: This function may need to be updated to match backend implementation");
  let data = { oId: oId, status: status };
  console.log(data);
  try {
    let res = await axios.post(`${apiURL}/api/order/update-order`, data);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const deleteOrder = async (oId) => {
  console.warn("deleteOrder: This function may need to be updated to match backend implementation");
  let data = { oId: oId };
  try {
    let res = await axios.post(`${apiURL}/api/order/delete-order`, data);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
