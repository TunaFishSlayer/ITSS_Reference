package com.darian.ecommerce.cart.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    // ID of the product
    private Long productId;

    // Quantity of the product in the cart
    private Integer quantity;
}
