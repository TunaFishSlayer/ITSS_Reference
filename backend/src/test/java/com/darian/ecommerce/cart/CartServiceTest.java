package com.darian.ecommerce.cart;

import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.cart.entity.Cart;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.cart.mapper.CartMapper;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.auth.UserService;
import com.darian.ecommerce.auth.enums.UserRole;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private User testUser;
    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .username("testuser")
                .email("test@example.com")
                .role(UserRole.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .build();

        testProduct = Product.builder()
                .productId(1L)
                .name("Test Product")
                .price(100.0f)
                .stockQuantity(10)
                .build();

        testCart = Cart.builder()
                .id(1L)
                .user(testUser)
                .total(0.0f)
                .items(new ArrayList<>())
                .build();

        testCartItem = CartItem.builder()
                .cart(testCart)
                .product(testProduct)
                .quantity(2)
                .productPrice(100.0f)
                .build();
    }

    @Test
    void testGetOrCreateCart_ExistingCart() {
        // Given
        when(userService.getUserById(1)).thenReturn(testUser);
        when(cartRepository.findByUser_Id(1)).thenReturn(Optional.of(testCart));

        // When
        Cart result = cartService.getOrCreateCart(1);

        // Then
        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUser().getId());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCart_NewCart() {
        // Given
        when(userService.getUserById(1)).thenReturn(testUser);
        when(cartRepository.findByUser_Id(1)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // When
        Cart result = cartService.getOrCreateCart(1);

        // Then
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testViewCart() {
        // Given
        List<CartItem> cartItems = List.of(testCartItem);
        CartDTO expectedCartDTO = CartDTO.builder()
                .cartId(1L)
                .userId(1)
                .items(List.of(CartItemDTO.builder()
                        .productId(1L)
                        .quantity(2)
                        .productPrice(100.0f)
                        .build()))
                .total(200.0f)
                .totalItems(2)
                .build();

        when(userService.getUserById(1)).thenReturn(testUser);
        when(cartRepository.findByUser_Id(1)).thenReturn(Optional.of(testCart));
        when(cartItemService.getCartItems(testCart)).thenReturn(cartItems);
        when(cartMapper.toDTO(testCart)).thenReturn(expectedCartDTO);

        // When
        CartDTO result = cartService.viewCart(1);

        // Then
        assertNotNull(result);
        assertEquals(expectedCartDTO.getCartId(), result.getCartId());
        assertEquals(expectedCartDTO.getUserId(), result.getUserId());
        assertEquals(expectedCartDTO.getTotal(), result.getTotal());
    }

    @Test
    void testCheckAvailability_EmptyCart() {
        // Given
        CartDTO emptyCartDTO = CartDTO.builder()
                .userId(1)
                .items(new ArrayList<>())
                .build();

        // When
        Boolean result = cartService.checkAvailability(emptyCartDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckAvailability_ValidCart() {
        // Given
        List<CartItemDTO> items = List.of(
                CartItemDTO.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()
        );
        
        CartDTO cartDTO = CartDTO.builder()
                .userId(1)
                .items(items)
                .totalItems(2)
                .build();

        when(productService.checkProductQuantity(1L, 2)).thenReturn(true);

        // When
        Boolean result = cartService.checkAvailability(cartDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckAvailability_ExceedsMaxItems() {
        // Given
        CartDTO cartDTO = CartDTO.builder()
                .userId(1)
                .totalItems(25) // Exceeds MAX_CART_ITEMS (20)
                .items(new ArrayList<>()) // Add empty items list to avoid null check
                .build();

        // When
        Boolean result = cartService.checkAvailability(cartDTO);

        // Then
        assertFalse(result);
    }

    @Test
    void testSave() {
        // Given
        when(cartRepository.save(testCart)).thenReturn(testCart);

        // When
        Cart result = cartService.save(testCart);

        // Then
        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        verify(cartRepository, times(1)).save(testCart);
    }

    @Test
    void testGetOrCreateCart_NullUserId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.getOrCreateCart(null);
        });
    }

    @Test
    void testSave_NullCart() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.save(null);
        });
    }

    @Test
    void testCheckAvailability_NullCart() {
        // When
        Boolean result = cartService.checkAvailability(null);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckAvailability_InvalidQuantity() {
        // Given
        List<CartItemDTO> items = List.of(
                CartItemDTO.builder()
                        .productId(1L)
                        .quantity(0) // Invalid quantity
                        .build()
        );

        CartDTO cartDTO = CartDTO.builder()
                .userId(1)
                .items(items)
                .totalItems(1)
                .build();

        // When
        Boolean result = cartService.checkAvailability(cartDTO);

        // Then
        assertFalse(result);
    }

    @Test
    void testCheckAvailability_ExcessiveItemQuantity() {
        // Given
        List<CartItemDTO> items = List.of(
                CartItemDTO.builder()
                        .productId(1L)
                        .quantity(100) // Exceeds MAX_ITEM_QUANTITY (99)
                        .build()
        );

        CartDTO cartDTO = CartDTO.builder()
                .userId(1)
                .items(items)
                .totalItems(1)
                .build();

        // When
        Boolean result = cartService.checkAvailability(cartDTO);

        // Then
        assertFalse(result);
    }

    @Test
    void testCheckAvailability_ProductNotAvailable() {
        // Given
        List<CartItemDTO> items = List.of(
                CartItemDTO.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()
        );

        CartDTO cartDTO = CartDTO.builder()
                .userId(1)
                .items(items)
                .totalItems(2)
                .build();

        when(productService.checkProductQuantity(1L, 2)).thenReturn(false);

        // When
        Boolean result = cartService.checkAvailability(cartDTO);

        // Then
        assertFalse(result);
    }
}
