import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// Create order - Updated to match backend endpoint
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

// Payment for order - Updated to match backend endpoint
export const payOrder = async (orderId, paymentData) => {
  try {
    let res = await axios.post(`${apiURL}/api/v1/orders/${orderId}/pay`, paymentData, {
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

// VNPay payment - Updated to match backend endpoint
export const createVNPayPayment = async (orderId, paymentData) => {
  try {
    let res = await axios.post(`${apiURL}/api/v1/payment/${orderId}`, paymentData, {
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

// Legacy Braintree functions - these may not be available in current backend
export const getBrainTreeToken = async () => {
  console.warn("getBrainTreeToken: This endpoint may not be available in the current backend");
  let uId = JSON.parse(localStorage.getItem("jwt")).user._id;
  try {
    let res = await axios.post(`${apiURL}/api/braintree/get-token`, {
      uId: uId,
    });
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const getPaymentProcess = async (paymentData) => {
  console.warn("getPaymentProcess: This endpoint may not be available in the current backend");
  try {
    let res = await axios.post(`${apiURL}/api/braintree/payment`, paymentData);
    return res.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
