package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.mapper.CartMapper;
import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.cart.entity.Cart;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.product.service.ProductService;
import com.darian.ecommerce.auth.UserService;
import com.darian.ecommerce.shared.constants.Constants;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final CartMapper cartMapper;



    // Constructor injection for dependencies
    public CartServiceImpl(CartRepository cartRepository,
                           ProductService productService,
                           UserService userService,
                           CartMapper cartMapper,
                           @Lazy CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
        this.cartMapper = cartMapper;
        this.cartItemService = cartItemService;
    }

    @Override
    @Transactional
    public Cart getOrCreateCart(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = userService.getUserById(userId);
        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    log.info("Creating new cart for user {}", userId);
                    Cart newCart = Cart.builder()
                            .user(user)
                            .total(0.0f)
                            .build();
                    Cart savedCart = save(newCart);
                    log.info("Created cart {} for user {}", savedCart.getId(), userId);
                    return savedCart;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO viewCart(Integer userId) {
        log.info(LoggerMessages.CART_VIEW, userId);
        Cart cart = getOrCreateCart(userId);
        List<CartItem> cartItems = cartItemService.getCartItems(cart);
        cart.setItems(cartItems);
        cart.updateTotal(); // Ensure total is up to date
        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public Cart save(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        cart.updateTotal(); // Always update total before saving
        return cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkAvailability(CartDTO cartDTO) {
        if (cartDTO == null) {
            return true; // Null cart is considered available
        }

        // Check if cart exceeds maximum items limit (check totalItems first)
        if (cartDTO.getTotalItems() != null && cartDTO.getTotalItems() > Constants.MAX_CART_ITEMS) {
            log.warn("Cart for user {} exceeds maximum items limit: {} > {}",
                    cartDTO.getUserId(), cartDTO.getTotalItems(), Constants.MAX_CART_ITEMS);
            return false;
        }

        // If no items, cart is available
        if (cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
            return true; // Empty cart is always available
        }

        // Check availability of each item
        for (var item : cartDTO.getItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                log.warn("Invalid quantity {} for product {}", item.getQuantity(), item.getProductId());
                return false;
            }

            if (item.getQuantity() > Constants.MAX_ITEM_QUANTITY) {
                log.warn("Quantity {} exceeds maximum allowed {} for product {}",
                        item.getQuantity(), Constants.MAX_ITEM_QUANTITY, item.getProductId());
                return false;
            }

            // Check product availability and stock
            if (!productService.checkProductQuantity(item.getProductId(), item.getQuantity())) {
                log.warn("Product {} is not available or insufficient stock for quantity {}",
                        item.getProductId(), item.getQuantity());
                return false;
            }
        }

        return true;
    }

}







