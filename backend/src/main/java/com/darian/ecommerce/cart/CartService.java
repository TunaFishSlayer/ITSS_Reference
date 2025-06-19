package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.cart.entity.Cart;

public interface CartService {

    Cart getOrCreateCart(Integer userId);

    // View the user's cart
    CartDTO viewCart(Integer userId);

    Cart save(Cart cart);

    // Check the availability of a cart
    Boolean checkAvailability(CartDTO cartDTO);
}

