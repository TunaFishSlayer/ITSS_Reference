package com.darian.ecommerce.order;

import com.darian.ecommerce.order.entity.DeliveryInfo;
import com.darian.ecommerce.order.entity.Order;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by status
    List<Order> findByOrderStatus(OrderStatus status);

    // Find orders by customer ID
    List<Order> findByUser_Id(Integer customerId);

    // Update order status
    default void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        findById(orderId).ifPresent(order -> {
            order.setOrderStatus(orderStatus);
            save(order);
        });
    }

    // Update order status
    default void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
        findById(orderId).ifPresent(order -> {
            order.setPaymentStatus(paymentStatus);
            save(order);
        });
    }

    // Update shipping fee
    default void updateShippingFee(Long orderId, Float fees) {
        findById(orderId).ifPresent(order -> {
            order.setShippingFee(fees);
            save(order);
        });
    }

    default void updateDeliveryInfo(Long orderId, DeliveryInfo deliveryInfo) {
        findById(orderId).ifPresent(order -> {
            order.setDeliveryInfo(deliveryInfo);
            save(order);
        });
    }
}