import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

const BearerToken = () =>
  localStorage.getItem("jwt")
    ? JSON.parse(localStorage.getItem("jwt")).token
    : false;
const Headers = () => {
  return {
    headers: {
      Authorization: `Bearer ${BearerToken()}`, // Fixed: Use Authorization instead of token
    },
  };
};

// ⚠️ WARNING: Backend does not have CategoryController
// These functions return mock data or throw errors
export const getAllCategory = async () => {
  console.warn("getAllCategory: Backend does not have CategoryController. Returning mock data.");
  try {
    // Return mock categories based on the data-init.sql
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
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const createCategory = async ({
  cName,
  cImage,
  cDescription,
  cStatus,
}) => {
  console.warn("createCategory: Backend does not have CategoryController. This operation is not supported.");
  return {
    success: false,
    message: "Category creation is not supported in the current backend implementation."
  };
};

export const editCategory = async (cId, des, status) => {
  console.warn("editCategory: Backend does not have CategoryController. This operation is not supported.");
  return {
    success: false,
    message: "Category editing is not supported in the current backend implementation."
  };
};

export const deleteCategory = async (cId) => {
  console.warn("deleteCategory: Backend does not have CategoryController. This operation is not supported.");
  return {
    success: false,
    message: "Category deletion is not supported in the current backend implementation."
  };
};
