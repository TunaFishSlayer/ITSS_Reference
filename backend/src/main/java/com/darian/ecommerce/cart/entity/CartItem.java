package com.darian.ecommerce.cart.entity;

import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.cart.id.CartItemId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    // Primary key
    @EmbeddedId
    private CartItemId id;

    // ID of the product
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    // Quantity of the product
    private Integer quantity;

    // Price of the product at the time of adding to cart
    @Column(name = "product_price")
    private Float productPrice;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
