package com.darian.ecommerce.order.dto;

import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseOrderDTO {
    private Long orderId;
    private Integer customerId;

    private OrderStatus orderStatus;
    private DeliveryInfoDTO deliveryInfo;
    private Float subtotal;
    private Float shippingFee;
    private Float total;
    private LocalDateTime createdDate;
    private Float discount;
    private PaymentStatus paymentStatus;
    private List<OrderItemDTO> items;
}
