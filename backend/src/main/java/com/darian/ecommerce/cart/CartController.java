package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.shared.constants.ApiEndpoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.CART)
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final CartItemService cartItemService;

    // Constructor injection for CartService
    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    // Add a product to the user's cart
    @PostMapping(ApiEndpoints.CART_ADD)
    public ResponseEntity<CartItemDTO> addProductToCart(@PathVariable Integer userId,
                                                        @RequestParam Long productId,
                                                        @RequestParam Integer quantity) {
        try {
            log.info("Adding product {} with quantity {} to cart for user {}", productId, quantity, userId);
            CartItemDTO cartItemDTO = cartItemService.addToCart(userId, productId, quantity);
            return ResponseEntity.ok(cartItemDTO);
        } catch (IllegalArgumentException e) {
            log.error("Error adding product to cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error adding product to cart", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // View the user's cart
    @GetMapping(ApiEndpoints.CART_BY_USER)
    public ResponseEntity<CartDTO> viewCart(@PathVariable Integer userId) {
        try {
            log.info("Viewing cart for user {}", userId);
            CartDTO cartDTO = cartService.viewCart(userId);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            log.error("Error viewing cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error viewing cart", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Update the quantity of a product in the user's cart
    @PutMapping(ApiEndpoints.CART_UPDATE)
    public ResponseEntity<CartItemDTO> updateCart(@PathVariable Integer userId,
                                              @RequestParam Long productId,
                                              @RequestParam Integer quantity) {
        try {
            log.info("Updating product {} quantity to {} in cart for user {}", productId, quantity, userId);
            CartItemDTO cartItemDTO = cartItemService.updateQuantity(userId, productId, quantity);
            return ResponseEntity.ok(cartItemDTO);
        } catch (IllegalArgumentException e) {
            log.error("Error updating cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error updating cart", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Remove a product from the user's cart
    @DeleteMapping(ApiEndpoints.CART_REMOVE)
    public ResponseEntity<CartItemDTO> removeFromCart(@PathVariable Integer userId,
                                                      @RequestParam Long productId) {
        try {
            log.info("Removing product {} from cart for user {}", productId, userId);
            CartItemDTO cartItemDTO = cartItemService.removeFromCart(userId, productId);
            return ResponseEntity.ok(cartItemDTO);
        } catch (IllegalArgumentException e) {
            log.error("Error removing from cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error removing from cart", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Empty the user's cart
    @DeleteMapping(ApiEndpoints.CART_EMPTY)
    public ResponseEntity<Void> emptyCart(@PathVariable Integer userId) {
        try {
            log.info("Emptying cart for user {}", userId);
            cartItemService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error emptying cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error emptying cart", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Check cart availability
    @PostMapping("/{userId}/check-availability")
    public ResponseEntity<Boolean> checkCartAvailability(@PathVariable Integer userId) {
        try {
            log.info("Checking cart availability for user {}", userId);
            CartDTO cartDTO = cartService.viewCart(userId);
            Boolean isAvailable = cartService.checkAvailability(cartDTO);
            return ResponseEntity.ok(isAvailable);
        } catch (IllegalArgumentException e) {
            log.error("Error checking cart availability: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error checking cart availability", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
