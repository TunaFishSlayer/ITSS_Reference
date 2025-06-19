package com.darian.ecommerce.payment;

import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.payment.dto.PaymentResult;
import com.darian.ecommerce.payment.dto.RefundResult;
import com.darian.ecommerce.order.entity.Order;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public interface PaymentService {


    // Cohesion: Functional Cohesion
    // → Interface chỉ định nghĩa các hành vi liên quan đến thanh toán và hoàn tiền.

    // SRP: Vi phạm nhẹ
    // → Gộp xử lý thanh toán và hoàn tiền vào cùng interface. Có thể chia thành `PaymentService` và `RefundService` để rõ ràng hơn.


    // Process payment for an order with a specified payment method
//    public PaymentResult payOrder(Long orderId, String paymentMethod) throws OrderNotFoundException;

//    PaymentResult payOrder(Long orderId, String paymentMethod, HttpServletRequest request) throws OrderNotFoundException;

    PaymentResult payOrder(Long orderId, String paymentMethod, HttpServletRequest request) throws OrderNotFoundException, UnsupportedEncodingException;

    // Validate payment details for an order
    Boolean validatePayment(Order order) throws OrderNotFoundException;

    //     Process refund for an order
    public RefundResult processRefund(Long orderId);

//     Check if cancellation is valid for an order
    public Boolean checkCancellationValidity(Long orderId);


    boolean handleVnPayIpn(Map<String, String> vnpParams);
}
