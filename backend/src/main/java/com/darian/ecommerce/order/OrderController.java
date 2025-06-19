package com.darian.ecommerce.order;

import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.order.dto.DeliveryInfoDTO;
import com.darian.ecommerce.order.dto.InvoiceDTO;
import com.darian.ecommerce.order.dto.OrderDTO;
import com.darian.ecommerce.shared.constants.ApiEndpoints;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiEndpoints.ORDERS)
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @PostMapping("/place")
//    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO) {
//        OrderDTO result = orderService.placeOrder(orderDTO);
//        return ResponseEntity.ok(result);
//    }
//
//    @PostMapping("/place-rush")
//    public ResponseEntity<RushOrderDTO> placeRushOrder(@RequestBody RushOrderDTO rushOrderDTO) {
//        RushOrderDTO result = orderService.placeRushOrder(rushOrderDTO);
//        return ResponseEntity.ok(result);
//    }

    @PostMapping(ApiEndpoints.ORDER_CANCEL)
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) throws OrderNotFoundException {
        log.info(LoggerMessages.ORDER_STATUS_CHANGED, "current", "CANCELLED", orderId);
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndpoints.ORDER_BY_ID)
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) throws OrderNotFoundException {
        OrderDTO result = orderService.getOrderDetails(orderId);
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

}
