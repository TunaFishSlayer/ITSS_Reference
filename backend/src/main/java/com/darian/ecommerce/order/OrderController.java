package com.darian.ecommerce.order;

import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.order.dto.*;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.shared.constants.ApiEndpoints;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.ORDERS)
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<SplitOrderDTO> placeOrderFromCart(@RequestBody CartDTO cartDTO) {
        SplitOrderDTO result = orderService.placeOrderFromCart(cartDTO);
        log.info(LoggerMessages.ORDER_CREATED, result);
        return ResponseEntity.ok(result);
        
    }

    @PostMapping(ApiEndpoints.ORDER_CANCEL)
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) throws OrderNotFoundException {
        log.info(LoggerMessages.ORDER_STATUS_CHANGED, "current", "CANCELLED", orderId);
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndpoints.ORDER_BY_ID)
    public ResponseEntity<BaseOrderDTO> getOrderDetails(@PathVariable Long orderId) throws OrderNotFoundException {
        BaseOrderDTO result = orderService.getOrderDetails(orderId);
        log.info(LoggerMessages.ORDER_UPDATED, orderId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(ApiEndpoints.ORDER_RUSH_DELIVERY)
    public ResponseEntity<RushOrderDTO> setRushDeliveryInfo(@PathVariable Long orderId,
                                                            @RequestBody RushOrderDeliveryInfoDTO rushOrderDeliveryInfoDTO) throws OrderNotFoundException {
        RushOrderDTO result = orderService.setRushDeliveryInfo(orderId, rushOrderDeliveryInfoDTO);
        log.info(LoggerMessages.ORDER_UPDATED, orderId);
        return ResponseEntity.ok(result);
    }


    @PutMapping(ApiEndpoints.ORDER_DELIVERY)
    public ResponseEntity<OrderDTO> setDeliveryInfo(@PathVariable Long orderId,
                                                    @RequestBody DeliveryInfoDTO deliveryInfoDTO) throws OrderNotFoundException {
        OrderDTO result = orderService.setDeliveryInfo(orderId, deliveryInfoDTO);
        log.info(LoggerMessages.ORDER_UPDATED, orderId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndpoints.ORDER_BY_ID + "/invoice")
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long orderId) throws OrderNotFoundException {
        InvoiceDTO result = orderService.getInvoice(orderId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<String> initiatePayment(@PathVariable Long orderId,
                                                  @RequestParam String paymentMethod) {
        // Placeholder for payment initiation
        return ResponseEntity.ok("Payment initiated for order: " + orderId);
    }

    @GetMapping
    public ResponseEntity<List<BaseOrderDTO>> getAllOrders(@RequestParam(required = false) OrderStatus status) {
        List<BaseOrderDTO> orders = orderService.getOrdersbyStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrderHistory(@PathVariable Integer customerId) {
        List<OrderDTO> history = orderService.getOrderHistory(customerId);
        return ResponseEntity.ok(history);
    }


    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable Long orderId) {
        orderService.setConfirmed(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long orderId) {
        orderService.setRejected(orderId);
        return ResponseEntity.noContent().build();
    }

    
}
