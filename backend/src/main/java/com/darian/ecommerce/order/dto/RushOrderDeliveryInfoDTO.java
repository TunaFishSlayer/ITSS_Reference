package com.darian.ecommerce.order.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RushOrderDeliveryInfoDTO {
private LocalDateTime rushDeliveryTime;
    private String deliveryInstruction;
}

