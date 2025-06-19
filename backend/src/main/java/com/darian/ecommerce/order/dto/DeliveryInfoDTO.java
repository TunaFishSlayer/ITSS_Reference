package com.darian.ecommerce.order.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoDTO {
    private String recipientName;
    private String phoneNumber;
    private String email;
    private String provinceCity;
    private String address;
    private String shippingInstructions;
}
