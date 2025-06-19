package com.darian.ecommerce.cart.mapper;

import com.darian.ecommerce.cart.dto.CartItemDTO;
import com.darian.ecommerce.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {
    public CartItemDTO toDTO(CartItem item) {
        return CartItemDTO.builder()
                .productId(item.getProduct().getProductId())
                .quantity(item.getQuantity())
                .build();
    }

    public  List<CartItemDTO> toDTOList(List<CartItem> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
