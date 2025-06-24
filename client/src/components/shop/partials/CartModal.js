import React, { Fragment, useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import { LayoutContext } from "../index";
import { cartListProduct } from "./FetchApi";
import { isAuthenticate } from "../auth/fetchApi";
import { cartList } from "../productDetails/Mixins";
import { subTotal, quantity, totalCost } from "./Mixins";

const apiURL = process.env.REACT_APP_API_URL;

const CartModal = () => {
  const history = useHistory();
  const { data, dispatch } = useContext(LayoutContext);
  const products = data.cartProduct;

  // State to track the item currently being edited for quantity
  const [editingQtyId, setEditingQtyId] = useState(null);

  const cartModalOpen = () =>
    dispatch({ type: "cartModalToggle", payload: !data.cartModal });

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchData = async () => {
    try {
      let responseData = await cartListProduct();
      if (responseData && responseData.Products) {
        dispatch({ type: "cartProduct", payload: responseData.Products });
        dispatch({ type: "cartTotalCost", payload: totalCost() });
      }
    } catch (error) {
      console.log(error);
    }
  };

  const removeCartProduct = (id) => {
    let cart = localStorage.getItem("cart")
      ? JSON.parse(localStorage.getItem("cart"))
      : [];
    if (cart.length !== 0) {
      cart = cart.filter((item) => item.id !== id);
      localStorage.setItem("cart", JSON.stringify(cart));
      fetchData();
      dispatch({ type: "inCart", payload: cartList() });
      dispatch({ type: "cartTotalCost", payload: totalCost() });
    }
    if (cart.length === 0) {
      dispatch({ type: "cartProduct", payload: null });
      fetchData();
      dispatch({ type: "inCart", payload: cartList() });
    }
  };

  // Update cart quantity
  const updateCartQuantity = (id, type, maxQty) => {
    let cart = localStorage.getItem("cart")
      ? JSON.parse(localStorage.getItem("cart"))
      : [];
    cart = cart.map((item) => {
      if (item.id === id) {
        let newQty = item.quantity;
        if (type === "increase" && newQty < maxQty) newQty++;
        if (type === "decrease" && newQty > 1) newQty--;
        return { ...item, quantity: newQty };
      }
      return item;
    });
    localStorage.setItem("cart", JSON.stringify(cart));
    fetchData();
    dispatch({ type: "inCart", payload: cartList() });
    dispatch({ type: "cartTotalCost", payload: totalCost() });
  };

  return (
    <Fragment>
      {/* Black Overlay */}
      <div
        className={`${
          !data.cartModal ? "hidden" : ""
        } fixed top-0 z-30 w-full h-full bg-black opacity-50`}
      />
      {/* Cart Modal Start */}
      <section
        className={`${
          !data.cartModal ? "hidden" : ""
        } fixed z-40 inset-0 flex items-start justify-end`}
      >
        <div
          style={{ background: "#303031" }}
          className="w-full md:w-5/12 lg:w-4/12 h-full flex flex-col justify-between"
        >
          <div className="overflow-y-auto">
            <div className="border-b border-gray-700 flex justify-between">
              <div className="p-4 text-white text-lg font-semibold">Cart</div>
              {/* Cart Modal Close Button */}
              <div className="p-4 text-white">
                <svg
                  onClick={cartModalOpen}
                  className="w-6 h-6 cursor-pointer"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    fillRule="evenodd"
                    d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
            </div>
            <div className="m-4 flex-col">
              {products &&
                products.length !== 0 &&
                products.map((item, index) => {
                  const curQty = quantity(item._id); // lấy quantity từ localStorage
                  return (
                    <Fragment key={index}>
                      {/* Cart Product Start */}
                      <div className="text-white flex space-x-2 my-4 items-center">
                        <img
                          className="w-16 h-16 object-cover object-center"
                          src={`${apiURL}/uploads/products/${item.pImages[0]}`}
                          alt="cartProduct"
                        />
                        <div className="relative w-full flex flex-col">
                          <div className="my-2">{item.pName}</div>
                          <div className="flex items-center justify-between">
                            <div className="flex items-center justify-between space-x-2">
                              <div className="text-sm text-gray-400">
                                Quantity :
                              </div>
                              <div className="flex items-end space-x-2">
                                {editingQtyId === item._id ? (
                                  <>
                                    {/* Nút giảm */}
                                    <span
                                      onClick={() =>
                                        updateCartQuantity(
                                          item.id,
                                          "decrease",
                                          item.pQuantity
                                        )
                                      }
                                      className={`select-none ${
                                        curQty <= 1 &&
                                        "opacity-40 cursor-not-allowed"
                                      }`}
                                      style={{
                                        pointerEvents:
                                          curQty <= 1 ? "none" : "auto",
                                      }}
                                    >
                                      <svg
                                        className="w-5 h-5 fill-current cursor-pointer"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                      >
                                        <path
                                          fillRule="evenodd"
                                          d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                                          clipRule="evenodd"
                                        />
                                      </svg>
                                    </span>
                                    <span className="font-semibold">{curQty}</span>
                                    {/* Nút tăng */}
                                    <span
                                      onClick={() =>
                                        updateCartQuantity(
                                          item.id,
                                          "increase",
                                          item.pQuantity
                                        )
                                      }
                                      className={`select-none ${
                                        curQty >= item.pQuantity &&
                                        "opacity-40 cursor-not-allowed"
                                      }`}
                                      style={{
                                        pointerEvents:
                                          curQty >= item.pQuantity
                                            ? "none"
                                            : "auto",
                                      }}
                                    >
                                      <svg
                                        className="w-5 h-5 fill-current cursor-pointer"
                                        fill="currentColor"
                                        viewBox="0 0 20 20"
                                      >
                                        <path
                                          fillRule="evenodd"
                                          d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                                          clipRule="evenodd"
                                        />
                                      </svg>
                                    </span>
                                  </>
                                ) : (
                                  <span className="font-semibold">{curQty}</span>
                                )}
                              </div>
                            </div>
                            <div>
                              <span className="text-sm text-gray-400">
                                Subtotal :
                              </span>
                              ${subTotal(item._id, item.pPrice)}.00
                            </div>
                          </div>
                          {/* Cart Product Remove & Edit Button */}
                          <div className="absolute top-0 right-0 text-white flex items-center space-x-2">
                            {/* Edit/Confirm button */}
                            {editingQtyId === item._id ? (
                              // Nếu đang chỉnh sửa, hiện icon tích (lưu)
                              <span
                                onClick={() => setEditingQtyId(null)}
                                className="mr-2 cursor-pointer"
                                title="Lưu"
                              >
                                <svg
                                  className="w-5 h-5 text-green-400"
                                  fill="currentColor"
                                  viewBox="0 0 20 20"
                                >
                                  <path
                                    fillRule="evenodd"
                                    d="M16.707 5.293a1 1 0 00-1.414 0L9 11.586l-2.293-2.293a1 1 0 10-1.414 1.414l3 3a1 1 0 001.414 0l7-7a1 1 0 000-1.414z"
                                    clipRule="evenodd"
                                  />
                                </svg>
                              </span>
                            ) : (
                              // Nếu chưa, hiện icon bút chì (edit)
                              <span
                                onClick={() => setEditingQtyId(item._id)}
                                className="mr-2 cursor-pointer"
                                title="Chỉnh sửa"
                              >
                                <svg
                                  className="w-5 h-5 text-blue-400"
                                  fill="none"
                                  stroke="currentColor"
                                  strokeWidth={2}
                                  viewBox="0 0 24 24"
                                >
                                  <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M15.232 5.232l3.536 3.536M9 13l6.586-6.586a2 2 0 112.828 2.828L11.828 15.828a2 2 0 01-2.828 0L9 13zm2 2l-4 4m6-6l2-2"
                                  />
                                </svg>
                              </span>
                            )}
                            {/* Remove button */}
                            <span
                              onClick={() => removeCartProduct(item._id)}
                              className="cursor-pointer"
                            >
                              <svg
                                className="w-5 h-5"
                                fill="currentColor"
                                viewBox="0 0 20 20"
                                xmlns="http://www.w3.org/2000/svg"
                              >
                                <path
                                  fillRule="evenodd"
                                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                  clipRule="evenodd"
                                />
                              </svg>
                            </span>
                          </div>
                        </div>
                      </div>
                      {/* Cart Product End */}
                    </Fragment>
                  );
                })}
              {products === null && (
                <div className="m-4 flex-col text-white text-xl text-center">
                  No product in cart
                </div>
              )}
            </div>
          </div>
          <div className="m-4 space-y-4">
            <div
              onClick={cartModalOpen}
              className="cursor-pointer px-4 py-2 border border-gray-400 text-white text-center cursor-pointer"
            >
              Continue shopping
            </div>
            {data.cartTotalCost ? (
              <Fragment>
                {isAuthenticate() ? (
                  <div
                    className="px-4 py-2 bg-black text-white text-center cursor-pointer"
                    onClick={() => {
                      history.push("/checkout");
                      cartModalOpen();
                    }}
                  >
                    Checkout ${data.cartTotalCost}.00
                  </div>
                ) : (
                  <div
                    className="px-4 py-2 bg-black text-white text-center cursor-pointer"
                    onClick={() => {
                      history.push("/");
                      cartModalOpen();
                      dispatch({
                        type: "loginSignupError",
                        payload: !data.loginSignupError,
                      });
                      dispatch({
                        type: "loginSignupModalToggle",
                        payload: !data.loginSignupModal,
                      });
                    }}
                  >
                    Checkout ${data.cartTotalCost}.00
                  </div>
                )}
              </Fragment>
            ) : (
              <div className="px-4 py-2 bg-black text-white text-center cursor-not-allowed">
                Checkout
              </div>
            )}
          </div>
        </div>
      </section>
      {/* Cart Modal End */}
    </Fragment>
  );
};

export default CartModal;