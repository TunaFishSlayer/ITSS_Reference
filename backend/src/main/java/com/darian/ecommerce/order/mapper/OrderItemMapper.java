package com.darian.ecommerce.order.mapper;

import com.darian.ecommerce.order.dto.OrderItemDTO;
import com.darian.ecommerce.order.entity.OrderItem;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

    private final ProductService productService;

    public OrderItemMapper(ProductService productService) {
        this.productService = productService;
    }


    public  OrderItemDTO toDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .productId(item.getProduct().getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }

    public  List<OrderItemDTO> toDTOList(List<OrderItem> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        Product product = productService.getProductById(dto.getProductId());

        return OrderItem.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .build();
    }

    public List<OrderItem> toEntityList(List<OrderItemDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
