package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.mapper.CartMapper;
import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.cart.entity.Cart;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.product.service.ProductService;
import com.darian.ecommerce.auth.UserService;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
    public Cart getOrCreateCart(Integer userId) {
        User user = userService.getUserById(userId);
        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    //TODO: gọi tới hàm tạo cart cũng được, có cả log cho mình luôn rồi
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return save(newCart);
                });
    }

    @Override
    public CartDTO viewCart(Integer userId) {
        log.info(LoggerMessages.CART_VIEW, userId);
        Cart cart = getOrCreateCart(userId);
        List<CartItem> cartItems = cartItemService.getCartItems(cart);
        cart.setItems(cartItems);
        return cartMapper.toDTO(cart);
    }

    @Override
    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }


    @Override
    public Boolean checkAvailability(CartDTO cartDTO) {
        return cartDTO.getItems().stream()
                .allMatch(item -> {
                    Product product = productService.getProductById(item.getProductId());
                    return product.getStockQuantity() >= item.getQuantity();
                });
    }


}

    //--------------------------------







