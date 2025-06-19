package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.shared.constants.ApiEndpoints;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.CART)
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;

    // Constructor injection for CartService
    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @RestController
    @RequestMapping("/api/test")
    public class TestController {
        @GetMapping
        public ResponseEntity<String> testEndpoint() {
            return ResponseEntity.ok("Backend is working!");
        }
    }

    // Add a product to the user's cart
    @PostMapping(ApiEndpoints.CART_ADD)
    public ResponseEntity<CartItemDTO> addProductToCart(@PathVariable Integer userId,
                                                        @RequestParam Long productId,
                                                        @RequestParam Integer quantity) {
        CartItemDTO cartItemDTO = cartItemService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(cartItemDTO);
    }

    // View the user's cart
    @GetMapping(ApiEndpoints.CART_BY_USER)
    public ResponseEntity<CartDTO> viewCart(@PathVariable Integer userId) {
        CartDTO cartDTO = cartService.viewCart(userId);
        return ResponseEntity.ok(cartDTO);
    }

    // Update the quantity of a product in the user's cart
    @PutMapping(ApiEndpoints.CART_UPDATE)
    public ResponseEntity<CartItemDTO> updateCart(@PathVariable Integer userId,
                                              @RequestParam Long productId,
                                              @RequestParam Integer quantity) {
        CartItemDTO cartItemDTO = cartItemService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(cartItemDTO);
    }

    // Remove a product from the user's cart
    @DeleteMapping(ApiEndpoints.CART_REMOVE)
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer userId,
                                               @RequestParam Long productId) {
        cartItemService.removeFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    // Empty the user's cart
    @DeleteMapping(ApiEndpoints.CART_EMPTY)
    public ResponseEntity<Void> emptyCart(@PathVariable Integer userId) {
        cartItemService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

}
