package com.darian.ecommerce.order.entity;

import com.darian.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    // Reference to the parent order (*-1 relationship)
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // ID of the product in the order
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Quantity of the product
    private Integer quantity;

    // Unit price of the product
    @Column(name = "unit_price")
    private Float unitPrice;




    // Calculate line total (quantity * unitPrice)
    public Float getLineTotal(){
        return quantity * unitPrice;
    }
}
