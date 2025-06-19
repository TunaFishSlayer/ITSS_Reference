package com.darian.ecommerce.cart.entity;

import com.darian.ecommerce.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    // Primary key (userId acts as cartId)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // User owning the cart
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // List of items in the cart (1-* relationship)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // Total cost of the cart
    @Column(name = "total")
    private Float total;

    // Update total based on items
    public void updateTotal() {
        this.total = (float) items.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
    }

    @PrePersist
    protected void onCreate() {
        this.total = 0f;
    }

}
