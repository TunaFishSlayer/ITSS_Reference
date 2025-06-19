package com.darian.ecommerce.order;

import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.order.entity.Order;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.order.dto.DeliveryInfoDTO;
import com.darian.ecommerce.order.dto.InvoiceDTO;
import com.darian.ecommerce.order.dto.OrderDTO;
import com.darian.ecommerce.order.dto.RushOrderDTO;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDTO createOrder(CartDTO cartDTO);

    OrderDTO getOrderDetails(Long orderId) throws OrderNotFoundException;

    InvoiceDTO getInvoice(Long orderId) throws OrderNotFoundException;

    Optional<Order> findOrderById(Long orderId);

    List<OrderDTO> getOrderHistory(Integer customerId);

    OrderDTO setDeliveryInfo(Long orderId, DeliveryInfoDTO deliveryInfoDTO) throws OrderNotFoundException;

    void setPending(Long orderId);

    OrderDTO placeOrder(OrderDTO orderDTO);

    RushOrderDTO placeRushOrder(RushOrderDTO rushOrderDTO);

    void cancelOrder(Long orderId) throws OrderNotFoundException;

    void updatePaymentStatus(Long orderId, PaymentStatus status);

    void updateOrderStatus(Long orderId, OrderStatus orderStatus);

    Boolean isRushOrder(Long orderId);

    Boolean checkAvailability(CartDTO cartDTO);

    Boolean validateDeliveryInfo(DeliveryInfoDTO deliveryInfoDTO);

    Boolean checkRushDeliveryAddress(String address);

    Boolean checkCancellationValidity(Long orderId);

    Boolean checkRushProductEligibility(Long productId);
}
