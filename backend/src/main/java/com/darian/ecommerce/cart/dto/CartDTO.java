package com.darian.ecommerce.cart.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    // ID of the cart
    private Long cartId;

    // ID of the user owning the cart
    private Integer userId;

    // List of items in the cart
    private List<CartItemDTO> items;

    // Total cost of the cart
    private Float total;

    // Total number of items in cartAdd commentMore actions
    private Integer totalItems;

    // Check if cart is empty
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
