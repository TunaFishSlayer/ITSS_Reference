package com.darian.ecommerce.payment;

import com.darian.ecommerce.subsystem.vnpay.VNPayConfig;
import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.payment.exception.InvalidPaymentMethodException;
import com.darian.ecommerce.payment.dto.PaymentResDTO;
import com.darian.ecommerce.payment.dto.PaymentResult;
import com.darian.ecommerce.payment.dto.RefundResult;
import com.darian.ecommerce.payment.enums.PaymentMethod;
import com.darian.ecommerce.subsystem.vnpay.VNPayResponseHandler;
import com.darian.ecommerce.shared.constants.ApiEndpoints;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping(ApiEndpoints.PAYMENT)
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:8080",
    "http://127.0.0.1:3000",
    "http://127.0.0.1:8080"
}, allowCredentials = "true")
public class PaymentController {

    // Cohesion: Functional Cohesion
    // → Class chỉ phục vụ xử lý HTTP request liên quan đến thanh toán (pay/refund), không chứa logic nghiệp vụ.

    // SRP: Không vi phạm
    // → Lớp này chỉ đóng vai trò cầu nối giữa HTTP request và service xử lý logic thanh toán.

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;
    private final VNPayResponseHandler vnPayResponseHandler;

    // Constructor injection for PaymentServiceImpl
    public PaymentController(PaymentService paymentService, VNPayResponseHandler vnPayResponseHandler) {
        this.paymentService = paymentService;
        this.vnPayResponseHandler = vnPayResponseHandler;
    }

    // Process payment for an order
    @PostMapping(ApiEndpoints.PAYMENT_BY_ORDER)
    public void payOrder(@PathVariable Long orderId,
                         @RequestParam String paymentMethod,
                         HttpServletRequest request,
                         HttpServletResponse response) throws InvalidPaymentMethodException, OrderNotFoundException, IOException {
        // Validate payment method
        try {
            log.info("🔁 INIT payOrder: orderId={}, method={}", orderId, paymentMethod);
            PaymentMethod.valueOf(paymentMethod.toUpperCase());
            log.info(LoggerMessages.PAYMENT_INIT, orderId, paymentMethod);
            PaymentResult result = paymentService.payOrder(orderId, paymentMethod, request);
            System.out.println("🔗 Redirecting to: " + result.getReturnUrl());
//            response.sendRedirect(result.getReturnUrl());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"url\": \"" + result.getReturnUrl() + "\"}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // In ra lỗi cụ thể để kiểm tra
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ERROR: " + e.getMessage());
            throw new InvalidPaymentMethodException(String.format(ErrorMessages.PAYMENT_INVALID_METHOD, paymentMethod));
        }

//        log.info(LoggerMessages.PAYMENT_COMPLETED, orderId, result.getPaymentStatus());
//        return ResponseEntity.ok(result);

    }

    // Process refund for an order
    @PostMapping(ApiEndpoints.PAYMENT_REFUND)
    public ResponseEntity<RefundResult> processRefund(@PathVariable Long orderId) {
        log.info(LoggerMessages.PAYMENT_INIT, orderId, "refund");
        RefundResult result = paymentService.processRefund(orderId);
        log.info(LoggerMessages.PAYMENT_COMPLETED, orderId, result.getRefundStatus());
        return ResponseEntity.ok(result);
    }

    @GetMapping("create-payment/{orderId}")
    public ResponseEntity<?> createPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentMethod,
            HttpServletRequest request) throws UnsupportedEncodingException, InvalidPaymentMethodException {

        try {
            PaymentMethod.valueOf(paymentMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentMethodException(String.format(ErrorMessages.PAYMENT_INVALID_METHOD, paymentMethod));
        }
//        String vnp_TxnRef = "254010";
        String vnp_TxnRef = String.valueOf(orderId);
        String orderInfo = "Thanh toan don hang:" + vnp_TxnRef;
        long amount = 99000 * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_PayCommand);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
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

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("OK");
        paymentResDTO.setMessage("Successfully");
        paymentResDTO.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }


//    @GetMapping("/vnpay-ipn")
//    public ResponseEntity<String> vnPayIpn(HttpServletRequest request) {
//        Map<String, String> vnpParams = new HashMap<>();
//        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
//            vnpParams.put(entry.getKey(), entry.getValue()[0]);
//        }
//
//        boolean isValid = vnPayResponseHandler.validateSignature(vnpParams); // Bạn cần xử lý xác thực hash
//        if (!isValid) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
//        }
//
//        // Gọi service cập nhật đơn hàng theo trạng thái
//        boolean updated = paymentService.handleVnPayIpn(vnpParams);
//        if (updated) {
//            return ResponseEntity.ok("OK");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to update order");
//        }
//    }
//
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmFromReturn(@RequestBody Map<String, String> body) {
        String txnRef = body.get("txnRef");
        String amount = body.get("amount");
        String status = body.get("status");
        String bankCode = body.get("bankCode");
        String payDate = body.get("payDate");
        String responseCode = body.get("responseCode");

        log.info("Xác nhận từ frontend - txnRef: {}, amount: {}, status: {}, bank: {}, payDate: {}, code: {}",
                txnRef, amount, status, bankCode, payDate, responseCode);

        if ("PAID".equals(status)) {
            // TODO: gọi service cập nhật đơn hàng thành công
        } else {
            // TODO: ghi nhận thanh toán thất bại nếu cần
        }

        return ResponseEntity.ok("Đã nhận thông tin từ returnUrl");
    }

}
