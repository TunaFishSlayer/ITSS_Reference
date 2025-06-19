package com.darian.ecommerce.payment.dto;

import com.darian.ecommerce.payment.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasePaymentResult {
    private TransactionType transactionType;
    private Long orderId;
    private String transactionId;
    private String errorMessage;
}
