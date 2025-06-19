//package com.darian.ecommerce.subsystem.vnpay;
//
//import com.darian.ecommerce.payment.dto.PaymentResDTO;
//import com.darian.ecommerce.payment.dto.PaymentResult;
//import com.darian.ecommerce.payment.dto.RefundResult;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class VNPaySubsystemAdapter {
//    // Cohesion: Functional Cohesion
//    // → Lớp này chỉ làm nhiệm vụ kết nối VNPaySubsystem với hệ thống chính bằng cách gọi các method nghiệp vụ đã được trừu tượng hóa.
//
//    // SRP: Không vi phạm
//    // → Là adapter đơn thuần theo đúng vai trò trong Adapter Pattern, không chứa logic xử lý.
//
//    private final VNPaySubsystemService vnPaySubsystemService;
//
//    // Constructor injection
//    public VNPaySubsystemAdapter(VNPaySubsystemService vnPaySubsystemService) {
//        this.vnPaySubsystemService = vnPaySubsystemService;
//    }
//
////    @Override
//    public PaymentResDTO initiatePayment(Long orderId, Float amount, String transactionContent, HttpServletRequest request) {
//        return vnPaySubsystemService.initiatePayment(orderId, amount, transactionContent, request);
//    }
//
//    // Process payment through subsystem
//    public PaymentResult processPayment(Long orderId, Float amount, String transactionContent, HttpServletRequest request) {
//        return vnPaySubsystemService.processPayment(orderId, amount, transactionContent);
//    }
//
//    // Process refund through subsystem
//    protected RefundResult processRefund(Long orderId) {
//        return vnPaySubsystemService.processRefund(orderId);
//    }
//
////    @Override
//    public PaymentResult handleIpnCallback(Map<String, String> vnpParams) {
//        return vnPaySubsystemService.handleIpnCallback(vnpParams);
//    }
//}
