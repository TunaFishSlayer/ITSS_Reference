package com.darian.ecommerce.cart.mapper;

import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.cart.entity.CartItem;
import com.darian.ecommerce.product.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {

    public CartItemDTO toDTO(CartItem item) {
        if (item == null) {
            return null;
        }

        Product product = item.getProduct();
        String imageUrl = null;

        // Get first image URL if available
        if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
            imageUrl = product.getImages().get(0).getUrl();
        }

        return CartItemDTO.builder()
                .productId(product != null ? product.getProductId() : null)
                .productName(product != null ? product.getName() : null)
                .productImageUrl(imageUrl)
                .quantity(item.getQuantity())
                .productPrice(item.getProductPrice())
                .lineTotal(item.getQuantity() * item.getProductPrice())
                .availableStock(product != null ? product.getStockQuantity() : 0)
                .build();
    }

    public List<CartItemDTO> toDTOList(List<CartItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
