package com.darian.ecommerce.cart.entity;

import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.shared.constants.Constants;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // User owning the cart (one-to-one relationship)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // List of items in the cart (1-* relationship)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    // Total cost of the cart
    @Column(name = "total")
    private Float total;

    // Timestamp when cart was created
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Timestamp when cart was last updated
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Update total based on items
    public void updateTotal() {
        if (items == null || items.isEmpty()) {
            this.total = 0.0f;
        } else {
            this.total = (float) items.stream()
                    .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                    .sum();
        }
        this.updatedAt = LocalDateTime.now();
    }

    // Get total number of items in cart
    public Integer getTotalItems() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    // Check if cart exceeds maximum items limit
    public boolean exceedsMaxItems() {
        return getTotalItems() > Constants.MAX_CART_ITEMS;
    }

    // Add item to cart
    public void addItem(CartItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setCart(this);
        updateTotal();
    }

    // Remove item from cart
    public void removeItem(CartItem item) {
        if (items != null) {
            items.remove(item);
            updateTotal();
        }
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.total == null) {
            this.total = 0.0f;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
