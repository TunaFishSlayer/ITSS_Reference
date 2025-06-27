package com.darian.ecommerce.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class PaymentConfirmDTO {
    private String vnpTransactionNo;   // Mã giao dịch do VNPAY cấp (dùng khi refund, query )
    private String vnpTxnRef;          // orderId
    private String vnpAmount;             // Số tiền thanh toán (đơn vị: VND nhân 100)
    private String vnpOrderInfo;
    private String vnpPayDate; // Ngày giờ thanh toán (vnp_PayDate) - định dạng yyyyMMddHHmmss
    private String vnpResponseCode;  // Trạng thái giao dịch (thường là "00" nếu thành công)
}
