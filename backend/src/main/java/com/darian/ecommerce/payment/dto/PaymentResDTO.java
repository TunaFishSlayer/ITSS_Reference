package com.darian.ecommerce.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResDTO {
    private String status;
    private String message;
    private String URL;
}
