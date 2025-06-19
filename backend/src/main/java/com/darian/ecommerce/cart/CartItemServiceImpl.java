package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.mapper.CartItemMapper;
import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.cart.entity.Cart;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.product.service.ProductService;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // Get or create cart for user
        Cart cart = cartService.getOrCreateCart(userId);

        // Get product
        Product product = productService.getProductById(productId);

        if (!productService.checkProductQuantity(productId, quantity ) ) {
            log.warn(LoggerMessages.CART_PRODUCT_NOT_AVAILABLE, productId, userId);
            throw new IllegalArgumentException(String.format(ErrorMessages.PRODUCT_NOT_AVAILABLE, productId));
        }

        CartItem cartItem;
        //TODO: có cần phải khai báo body của hàm find ko hay chỉ cần find by là được ?
        try {
            cartItem = getCartItem(cart,product);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            log.info(LoggerMessages.CART_UPDATE_QUANTITY, productId, cartItem.getQuantity(), userId);
        }
        catch (IllegalArgumentException e){
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
        }
        cartItemRepository.save(cartItem);

        //TODO: có cần phải update cartItem ở trong cart trong db ko , bên dưới là thêm mới vào cart thì ko nói rồi, cái này là lấy giá trị cartItem ra rồi update cái đó thì phải update lại vào cart chứ nhỉ ?

        return cartItemMapper.toDTO(cartItem);
    }


    @Override
    @Transactional
    public CartItemDTO updateQuantity(Integer userId, Long productId, Integer quantity) {
        log.info(LoggerMessages.CART_UPDATE_QUANTITY, productId, quantity, userId);
        Cart cart = cartService.getOrCreateCart(userId);

        Product product = productService.getProductById(productId);

        CartItem cartItem = getCartItem(cart,product);

        cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        log.info(LoggerMessages.CART_UPDATE_QUANTITY, userId, productId, quantity);

        //TODO update cart total also ?
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
        Cart cart = cartService.getOrCreateCart(userId);

        Product product = productService.getProductById(productId);

        CartItem cartItem = getCartItem(cart, product);

        cartItemRepository.delete(cartItem);

        log.info(LoggerMessages.CART_REMOVE_PRODUCT, userId, productId);
        return cartItemMapper.toDTO(cartItem);
    }

    @Transactional
    @Override
    public void clearCart(Integer userId) {
        log.info(LoggerMessages.CART_CLEARING, userId);
        Cart cart = cartService.getOrCreateCart(userId);
        cartItemRepository.deleteByCart(cart);
        log.info(LoggerMessages.CART_CLEARED, userId);
    }
}
