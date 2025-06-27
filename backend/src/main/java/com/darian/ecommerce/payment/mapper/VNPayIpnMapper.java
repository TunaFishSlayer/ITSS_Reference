package com.darian.ecommerce.payment.mapper;

import com.darian.ecommerce.payment.dto.PaymentConfirmDTO;

import java.util.Map;

public class VNPayIpnMapper {
    public static PaymentConfirmDTO toDTO(Map<String, String> params) {
        return PaymentConfirmDTO.builder()
                .vnpTransactionNo(params.get("vnp_TransactionNo"))
                .vnpTxnRef(params.get("vnp_TxnRef"))
                .vnpAmount(params.get("vnp_Amount"))
                .vnpOrderInfo(params.get("vnp_OrderInfo"))
                .vnpPayDate(params.get("vnp_PayDate"))
                .vnpResponseCode(params.get("vnp_ResponseCode"))
                .build();
    }
}
