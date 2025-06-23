package com.darian.ecommerce.order.dto;

public record SplitOrderDTO(RushOrderDTO rushOrder, BaseOrderDTO standardOrder) {
}
