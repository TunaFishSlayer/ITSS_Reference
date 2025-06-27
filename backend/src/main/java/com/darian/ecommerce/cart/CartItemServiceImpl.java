package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.mapper.CartItemMapper;
import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.cart.entity.Cart;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.cart.id.CartItemId;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.product.service.ProductService;
import com.darian.ecommerce.shared.constants.Constants;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private static final Logger log = LoggerFactory.getLogger(CartItemServiceImpl.class);

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartItemMapper cartItemMapper;


    // Constructor injection for dependencies
    public CartItemServiceImpl(
                               CartItemRepository cartItemRepository,
                               ProductService productService,
                               CartItemMapper cartItemMapper,
                               CartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.cartItemMapper = cartItemMapper;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public CartItemDTO addToCart(Integer userId, Long productId, Integer quantity) {
        log.info(LoggerMessages.CART_ADD_PRODUCT, userId, productId, quantity);

        // Validate input parameters
        if (userId == null || productId == null || quantity == null) {
            throw new IllegalArgumentException("User ID, Product ID, and Quantity are required");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (quantity > Constants.MAX_ITEM_QUANTITY) {
            throw new IllegalArgumentException(String.format("Quantity cannot exceed %d", Constants.MAX_ITEM_QUANTITY));
        }

        // Get or create cart for user
        Cart cart = cartService.getOrCreateCart(userId);

        // Check if cart already has maximum items
        if (cart.getTotalItems() >= Constants.MAX_CART_ITEMS) {
            throw new IllegalArgumentException(String.format("Cart cannot have more than %d items", Constants.MAX_CART_ITEMS));
        }

        // Get product
        Product product = productService.getProductById(productId);

        // Check product availability and stock
        if (!productService.checkProductQuantity(productId, quantity)) {
            log.warn(LoggerMessages.CART_PRODUCT_NOT_AVAILABLE, productId, userId);
            throw new IllegalArgumentException(String.format(ErrorMessages.PRODUCT_NOT_AVAILABLE, productId));
        }

        // Check if item already exists in cart
        CartItem cartItem;
        try {
            cartItem = getCartItem(cart, product);
            // Item exists, update quantity
            int newQuantity = cartItem.getQuantity() + quantity;

            // Check if new quantity exceeds maximum
            if (newQuantity > Constants.MAX_ITEM_QUANTITY) {
                throw new IllegalArgumentException(String.format("Total quantity cannot exceed %d", Constants.MAX_ITEM_QUANTITY));
            }

            cartItem.setQuantity(newQuantity);
            log.info("Updated quantity for product {} to {} in cart for user {}", productId, newQuantity, userId);
        } catch (IllegalArgumentException e) {
            // Item doesn't exist, create new cart item
            CartItemId cartItemId = new CartItemId(cart.getId(), productId);
            cartItem = CartItem.builder()
                    .id(cartItemId)
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .productPrice(product.getPrice())
                    .build();

            log.info("Added new product {} with quantity {} to cart for user {}", productId, quantity, userId);
        }

        // Save cart item
        cartItem = cartItemRepository.save(cartItem);

        // Update cart total and save cart
        cart.updateTotal();
        cartService.save(cart);

        return cartItemMapper.toDTO(cartItem);
    }


    @Override
    @Transactional
    public CartItemDTO updateQuantity(Integer userId, Long productId, Integer quantity) {
        log.info(LoggerMessages.CART_UPDATE_QUANTITY, productId, quantity, userId);

        // Validate input parameters
        if (userId == null || productId == null || quantity == null) {
            throw new IllegalArgumentException("User ID, Product ID, and Quantity are required");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (quantity > Constants.MAX_ITEM_QUANTITY) {
            throw new IllegalArgumentException(String.format("Quantity cannot exceed %d", Constants.MAX_ITEM_QUANTITY));
        }

        Cart cart = cartService.getOrCreateCart(userId);
        Product product = productService.getProductById(productId);

        // Check product availability and stock
        if (!productService.checkProductQuantity(productId, quantity)) {
            log.warn(LoggerMessages.CART_PRODUCT_NOT_AVAILABLE, productId, userId);
            throw new IllegalArgumentException(String.format(ErrorMessages.PRODUCT_NOT_AVAILABLE, productId));
        }

        CartItem cartItem = getCartItem(cart, product);
        cartItem.setQuantity(quantity);
        cartItem.setProductPrice(product.getPrice()); // Update price in case it changed

        cartItemRepository.save(cartItem);
        log.info(LoggerMessages.CART_UPDATE_QUANTITY, userId, productId, quantity);

        // Update cart total and save cart
        cart.updateTotal();
        cartService.save(cart);

        return cartItemMapper.toDTO(cartItem);
    }


    @Transactional(readOnly = true)
    @Override
    public List<CartItem> getCartItems(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    @Override
    public CartItem getCartItem(Cart cart, Product product) {
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.CART_ITEM_NOT_FOUND, product.getProductId())));
        return cartItem;
    }

    @Override
    @Transactional
    public CartItemDTO removeFromCart(Integer userId, Long productId) {
        log.info(LoggerMessages.CART_REMOVE_PRODUCT, productId, userId);

        // Validate input parameters
        if (userId == null || productId == null) {
            throw new IllegalArgumentException("User ID and Product ID are required");
        }

        Cart cart = cartService.getOrCreateCart(userId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = getCartItem(cart, product);

        // Store DTO before deletion
        CartItemDTO cartItemDTO = cartItemMapper.toDTO(cartItem);

        // Delete cart item
        cartItemRepository.delete(cartItem);

        // Update cart total and save cart
        cart.updateTotal();
        cartService.save(cart);

        log.info(LoggerMessages.CART_REMOVE_PRODUCT, userId, productId);
        return cartItemDTO;
    }

    @Transactional
    @Override
    public void clearCart(Integer userId) {
        log.info(LoggerMessages.CART_CLEARING, userId);

        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        Cart cart = cartService.getOrCreateCart(userId);
        if (cart != null) {
            cart.getItems().clear(); // Sửa lại dùng clear() thay vì setItems(new ArrayList<>())
            cart.setTotal(0f); // Nếu có thuộc tính total thì reset lại
            cartService.save(cart);
        }

        // Xoá từng item khỏi DB và xóa khỏi cart.items
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        for (CartItem item : cartItems) {
            cartItemRepository.delete(item);
        }

        // Loại bỏ tham chiếu trong bộ nhớ tránh Hibernate bị lỗi
        if (!(cart.getItems() instanceof java.util.ArrayList)) {
            cart.setItems(new java.util.ArrayList<>(cart.getItems()));
        }
        cart.getItems().clear();

        // Cập nhật lại tổng tiền
        cart.updateTotal();

        // Lưu lại cart
        cartService.save(cart);

        log.info(LoggerMessages.CART_CLEARED, userId);
    }
}
