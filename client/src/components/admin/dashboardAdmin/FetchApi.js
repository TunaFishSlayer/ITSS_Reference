import axios from "axios";
const apiURL = process.env.REACT_APP_API_URL;

// ⚠️ WARNING: Backend does not have CustomizeController
// These functions return mock data or use alternative endpoints

export const DashboardData = async () => {
  console.warn("DashboardData: Backend does not have CustomizeController. Returning mock dashboard data.");
  try {
    // Use available endpoints to get real data
    const [ordersRes, productsRes] = await Promise.all([
      axios.get(`${apiURL}/api/v1/orders`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem("jwt") ? JSON.parse(localStorage.getItem("jwt")).token : ""}`
        }
      }).catch(() => ({ data: [] })),
      axios.get(`${apiURL}/api/v1/products/manager`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem("jwt") ? JSON.parse(localStorage.getItem("jwt")).token : ""}`
        }
      }).catch(() => ({ data: [] }))
    ]);

    // Calculate mock dashboard statistics
    const orders = ordersRes.data || [];
    const products = productsRes.data || [];

    return {
      success: true,
      totalData: {
        totalOrders: orders.length,
        totalProducts: products.length,
        totalUsers: 12, // From data-init.sql
        totalCategories: 9, // From data-init.sql
        totalRevenue: orders.reduce((sum, order) => sum + (order.total || 0), 0)
      }
    };
  } catch (error) {
    console.log(error);
    // Return fallback mock data
    return {
      success: true,
      totalData: {
        totalOrders: 3,
        totalProducts: 18,
        totalUsers: 12,
        totalCategories: 9,
        totalRevenue: 6300000
      }
    };
  }
};

export const getSliderImages = async () => {
  console.warn("getSliderImages: Backend does not have CustomizeController. Returning mock slider images.");
  try {
    // Return mock slider images
    return {
      success: true,
      sliderImages: [
        {
          _id: 1,
          image: "/placeholder-slider-1.jpg",
          title: "Welcome to Marketly",
          description: "Your one-stop e-commerce solution"
        },
        {
          _id: 2,
          image: "/placeholder-slider-2.jpg",
          title: "Best Products",
          description: "Quality products at great prices"
        }
      ]
    };
  } catch (error) {
    console.log(error);
    return { success: false, sliderImages: [] };
  }
};

export const postUploadImage = async (formData) => {
  console.warn("postUploadImage: Backend does not have CustomizeController. This operation is not supported.");
  try {
    // Mock successful upload
    return {
      success: true,
      message: "Image uploaded successfully (mock response)",
      image: {
        _id: Date.now(),
        image: "/placeholder-uploaded.jpg",
        title: "New Slider Image",
        description: "Uploaded image"
      }
    };
  } catch (error) {
    console.log(error);
    return { success: false, message: "Upload failed" };
  }
};

export const postDeleteImage = async (id) => {
  console.warn("postDeleteImage: Backend does not have CustomizeController. This operation is not supported.");
  try {
    // Mock successful deletion
    return {
      success: true,
      message: "Image deleted successfully (mock response)"
    };
  } catch (error) {
    console.log(error);
    return { success: false, message: "Delete failed" };
  }
};
