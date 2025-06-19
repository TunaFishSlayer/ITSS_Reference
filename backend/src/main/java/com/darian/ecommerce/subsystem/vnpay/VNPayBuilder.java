package com.darian.ecommerce.subsystem.vnpay;

import com.darian.ecommerce.payment.dto.VNPayRequest;
import com.darian.ecommerce.payment.enums.TransactionType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class VNPayBuilder {
    // Cohesion: Functional Cohesion
    // → Class này tập trung 100% vào việc tạo VNPayRequest từ dữ liệu domain, không làm nhiệm vụ nào khác.

    // SRP: Không vi phạm
    // → Lớp chỉ đảm nhiệm việc xây dựng request (builder cho transaction).

    private static final Logger log = LoggerFactory.getLogger(VNPayBuilder.class);

    private static Map<String, String> buildPaymentParams(Long orderId, Float amount,HttpServletRequest request) {
        String vnp_TxnRef = String.valueOf(orderId);
        String orderInfo = "Thanh toan don hang:" + vnp_TxnRef;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_PayCommand);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount.intValue()*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef); //String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", VNPayConfig.vnp_OrderType);
        vnp_Params.put("vnp_Locale", VNPayConfig.vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));
//        vnp_Params.put("vnp_IpnUrl", VNPayConfig.vnp_IpnUrl);
        vnp_Params.put("vnp_CreateDate" , VNPayConfig.getCreateDate());
        vnp_Params.put("vnp_ExpireDate", VNPayConfig.getExpireDate());
        return vnp_Params;
    }

    public static String buildPaymentUrl(Long orderId, Float amount,HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> vnp_Params = buildPaymentParams(orderId,amount,request);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        log.info("========== VNPAY PAYMENT INFO ==========");
        log.info("Hash Data: " + hashData.toString());
        log.info("Query URL: " + queryUrl);
        log.info("Payment URL: " + paymentUrl);
        log.info("=======================================");

        return paymentUrl;
    }


    protected VNPayRequest buildPaymentRequest(Long orderId, Float amount, String content) {
        return VNPayRequest.builder()
                .orderId(orderId)
                .amount(amount)
                .content(content)
                .transactionType(TransactionType.PAYMENT)
                .bankCode("NCB")
                .language("vn")
                .build();
    }

    protected VNPayRequest buildRefundRequest(Long orderId) {
        return VNPayRequest.builder()
                .orderId(orderId)
                .transactionType(TransactionType.REFUND)
                .build();
    }

}
