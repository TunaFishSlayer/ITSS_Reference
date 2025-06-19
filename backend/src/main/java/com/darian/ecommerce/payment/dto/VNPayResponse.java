package com.darian.ecommerce.payment.dto;

import com.darian.ecommerce.payment.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayResponse {
    private String transactionId;
    private String status;
    private TransactionType transactionType;
    private String responseCode;
    private String message;
    private String bankCode;
    private String bankTranNo;
    private String cardType;
    private String payDate;
    private String orderInfo;
    private Long amount;
}
