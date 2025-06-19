package com.darian.ecommerce.cart.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    // ID of the user owning the cart
    private Integer userId;

    // List of items in the cart
    private List<CartItemDTO>  items;
    
}
