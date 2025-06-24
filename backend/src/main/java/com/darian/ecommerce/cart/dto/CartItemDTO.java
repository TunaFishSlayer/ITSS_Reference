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

    // Name of the product
    private String productName;

    // Image URL of the product
    private String productImageUrl;

    // Quantity of the product in the cart
    private Integer quantity;

    // Price of the product at the time of adding to cart
    private Float productPrice;

    // Line total (quantity * productPrice)
    private Float lineTotal;

    // Calculate line total
    public Float getLineTotal() {
        if (quantity != null && productPrice != null) {
            return quantity * productPrice;
        }
        return 0.0f;
    }
}
