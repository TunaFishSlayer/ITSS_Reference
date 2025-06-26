import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Get all orders (admin)
export const getAllOrder = async () => {
  try {
    let res = await axios.get(`${apiURL}/api/order/get-all-orders`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

// Edit order status (admin)
export const editOrder = async (oId, status) => {
  let data = { oId: oId, status: status };
  try {
    let res = await axios.post(`${apiURL}/api/order/update-order`, data);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};


// Get order by ID (user/admin)
export const getOrderById = async (orderId) => {
  try {
    let res = await axios.get(`${apiURL}/api/v1/orders/${orderId}`);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
