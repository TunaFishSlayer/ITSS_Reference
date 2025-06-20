package com.darian.ecommerce.order.dto;

import lombok.*;

//@SuperBuilder
@Getter
@Setter
public class OrderDTO extends BaseOrderDTO{
    private boolean isRushOrder; // Indicates if this is a rush order
    // Inherits all fields from BaseOrderDTO
}
