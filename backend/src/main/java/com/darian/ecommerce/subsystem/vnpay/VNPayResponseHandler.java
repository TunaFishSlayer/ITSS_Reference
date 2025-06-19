package com.darian.ecommerce.subsystem.vnpay;

import com.darian.ecommerce.payment.exception.PaymentProcessingException;
import com.darian.ecommerce.payment.dto.PaymentResult;
import com.darian.ecommerce.payment.dto.RefundResult;
import com.darian.ecommerce.payment.dto.VNPayResponse;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.payment.enums.RefundStatus;
import com.darian.ecommerce.payment.enums.VNPayResponseStatus;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class VNPayResponseHandler {
    private static final Logger log = LoggerFactory.getLogger(VNPayResponseHandler.class);
    // Cohesion: Functional Cohesion
    // → Mọi method đều xử lý kết quả trả về từ VNPay (convert response → result object).

    // SRP: Không vi phạm
    // → Class này chỉ xử lý mapping giữa `VNPayResponse` và kết quả nghiệp vụ (PaymentResult, RefundResult).

    // Convert VNPayResponse to PaymentResult
    protected PaymentResult toPaymentResult(VNPayResponse response) {
        log.info(LoggerMessages.VNPAY_PAYMENT_EXECUTED, response.getTransactionId());
        if (response.getStatus().equals(VNPayResponseStatus.FAILURE)) {
            throw new PaymentProcessingException(String.format(ErrorMessages.VNPAY_TRANSACTION_FAILED, response.getMessage()));
        }
        return PaymentResult.builder()
                .transactionId(response.getTransactionId())
                .transactionType(response.getTransactionType())
                .transactionContent("Payment for " + response.getTransactionId())
                .transactionDate(LocalDateTime.now())
                .totalAmount(response.getAmount() != null ? response.getAmount().floatValue() : 0F)
                .paymentStatus(PaymentStatus.PAID)
                .build();
    }

    // Convert VNPayResponse to RefundResult
    protected RefundResult toRefundResult(VNPayResponse response) {
        log.info(LoggerMessages.VNPAY_REFUND_EXECUTED, response.getTransactionId());
        if (response.getStatus().equals(VNPayResponseStatus.FAILURE)) {
            throw new PaymentProcessingException(String.format(ErrorMessages.VNPAY_REFUND_FAILED, response.getMessage()));
        }
        return RefundResult.builder()
                .transactionId(response.getTransactionId())
                .transactionType(response.getTransactionType())
                .refundDate(LocalDateTime.now())
                .refundStatus(RefundStatus.REFUNDED)
                .build();
    }

    public PaymentResult parseIpnResponse(Map<String, String> vnpParams) {
        String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
        String vnp_TransactionStatus = vnpParams.get("vnp_TransactionStatus");
        String vnp_TxnRef = vnpParams.get("vnp_TxnRef");
        String vnp_Amount = vnpParams.get("vnp_Amount");
        String vnp_OrderInfo = vnpParams.get("vnp_OrderInfo");

        PaymentResult result = new PaymentResult();
        result.setTransactionId(vnp_TxnRef);
        result.setTotalAmount(vnp_Amount != null ? Float.parseFloat(vnp_Amount) / 100 : 0F);
        result.setTransactionContent(vnp_OrderInfo);
        result.setTransactionDate(LocalDateTime.now());

        if ("00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus)) {
            result.setPaymentStatus(PaymentStatus.PAID);
        } else {
            result.setPaymentStatus(PaymentStatus.FAILED);
            result.setErrorMessage("Transaction failed with response code: " + vnp_ResponseCode);
        }

        return result;
    }

    public boolean validateSignature(Map<String, String> vnpParams) {
        return VNPaySubsystemService.verifyChecksum(vnpParams); // Delegate to VNPaySubsystemService for consistency
    }
}


