package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.cart.entity.Cart;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.product.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemService {
    // Add a product to the user's cart
    CartItemDTO addToCart(Integer userId, Long productId, Integer quantity);

    // Update the quantity of a product in the user's cart
    CartItemDTO updateQuantity(Integer userId, Long productId, Integer quantity);

    @Transactional(readOnly = true)
    List<CartItem> getCartItems(Cart cart);

    CartItem getCartItem(Cart cart, Product product);

    // Remove a product from the user's cart
    CartItemDTO removeFromCart(Integer userId, Long productId);

    @Transactional
    void clearCart(Integer userId);
}
