import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

export const isAuthenticate = () =>
  localStorage.getItem("jwt") ? JSON.parse(localStorage.getItem("jwt")) : false;

export const isAdmin = () =>
  localStorage.getItem("jwt")
    ? JSON.parse(localStorage.getItem("jwt")).user.role === 1
    : false;

// Login request - Updated to match backend endpoint
export const loginReq = async ({ email, password }) => {
  const data = { email, password };
  try {
    let res = await axios.post(`${apiURL}/api/v1/auth/login`, data, {
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

// Signup request - Updated to match backend endpoint
export const signupReq = async ({ name, email, password, cPassword }) => {
  const data = {
    name,
    email,
    password,
    confirmPassword: cPassword // Backend might expect confirmPassword instead of cPassword
  };
  try {
    let res = await axios.post(`${apiURL}/api/v1/auth/register`, data, {
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
