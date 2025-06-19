package com.darian.ecommerce.order;

import com.darian.ecommerce.audit.AuditLogService;
import com.darian.ecommerce.auth.UserService;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.order.mapper.DeliveryInfoMapper;
import com.darian.ecommerce.order.mapper.OrderMapper;
import com.darian.ecommerce.order.businesslogic.shippingfee.ShippingFeeCalculatorFactory;
import com.darian.ecommerce.cart.CartService;
import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.audit.enums.ActionType;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.auth.enums.UserRole;
import com.darian.ecommerce.order.dto.DeliveryInfoDTO;
import com.darian.ecommerce.order.dto.InvoiceDTO;
import com.darian.ecommerce.order.dto.OrderDTO;
import com.darian.ecommerce.order.dto.RushOrderDTO;
import com.darian.ecommerce.order.entity.Order;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ShippingFeeCalculatorFactory calculatorFactory;
    private final CartService cartService;
    private final UserService userService;
    private final AuditLogService auditLogService;
    private final OrderMapper orderMapper;
    private final DeliveryInfoMapper deliveryInfoMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                          ShippingFeeCalculatorFactory calculatorFactory,
                          CartService cartService,
                          UserService userService,
                          AuditLogService auditLogService,
                          OrderMapper orderMapper,
                          DeliveryInfoMapper deliveryInfoMapper) {
        this.orderRepository = orderRepository;
        this.calculatorFactory = calculatorFactory;
        this.cartService = cartService;
        this.userService = userService;
        this.auditLogService = auditLogService;
        this.orderMapper = orderMapper;
        this.deliveryInfoMapper = deliveryInfoMapper;
    }

    @Override
    public OrderDTO createOrder(CartDTO cartDTO) {
        if (!checkAvailability(cartDTO)) {
            throw new IllegalStateException(String.format(ErrorMessages.VALIDATION_FAILED, "cart items not available"));
        }
        Order order = new Order();
//        order.setOrderId("ORD-" + System.currentTimeMillis());
        // need more checking + innovate cart service
        User user = userService.getUserById(cartDTO.getUserId());
        order.setUser(user);
        //check more
//        order.setItems(cartDTO.getItems());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedDate(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        logger.info(LoggerMessages.ORDER_CREATED, savedOrder.getOrderId());
        auditLogService.logOrderAction(order.getUser().getId(), order.getOrderId(), UserRole.CUSTOMER, ActionType.ORDER_ACTION);
        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO, false);
        order.setShippingFee(calculatorFactory.getCalculator(orderDTO).calculateShippingFee(orderDTO));
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        
        logger.info(LoggerMessages.ORDER_CREATED, savedOrder.getOrderId());
        auditLogService.logOrderAction(order.getUser().getId(), order.getOrderId(), UserRole.CUSTOMER, ActionType.PLACE_ORDER);
        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    public RushOrderDTO placeRushOrder(RushOrderDTO rushOrderDTO) {
        //cần xem lại logic của checkRushProductEligibility vì nó check từng item trong order chứ have to  check userId
        if (!checkRushProductEligibility(rushOrderDTO.getOrderId()) ||
                !checkRushDeliveryAddress(rushOrderDTO.getDeliveryInfo().getAddress())) {
            throw new IllegalStateException(String.format(ErrorMessages.VALIDATION_FAILED, "rush order not eligible"));
        }
        //need check
        Order order = orderMapper.toEntity(rushOrderDTO, true);
        order.setShippingFee(calculatorFactory.getCalculator(rushOrderDTO).calculateShippingFee(rushOrderDTO));
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        
        logger.info(LoggerMessages.ORDER_CREATED, savedOrder.getOrderId());
        auditLogService.logOrderAction(order.getUser().getId(), order.getOrderId(), UserRole.CUSTOMER, ActionType.PLACE_ORDER);
        return orderMapper.toRushOrderDTO(savedOrder, rushOrderDTO.getRushDeliveryTime());
    }

    @Override
    public OrderDTO getOrderDetails(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    public InvoiceDTO getInvoice(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setOrderId(orderId);
        invoice.setShippingFee(order.getShippingFee());
        invoice.setTotal(order.getTotal());
        return invoice;
    }

    @Override
    public void cancelOrder(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
                
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(String.format(ErrorMessages.ORDER_ALREADY_CANCELLED, orderId));
        }
        
        if (!checkCancellationValidity(orderId)) {
            throw new IllegalStateException(String.format(ErrorMessages.ORDER_CANNOT_BE_MODIFIED, orderId));
        }
        
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        logger.info(LoggerMessages.ORDER_STATUS_CHANGED, order.getOrderStatus(), OrderStatus.CANCELLED, orderId);
        auditLogService.logOrderAction(order.getUser().getId(), orderId, UserRole.CUSTOMER, ActionType.CANCEL_ORDER);
    }

    @Override
    public Boolean validateDeliveryInfo(DeliveryInfoDTO deliveryInfoDTO) {
        //TODO: need more conditions here
        // return deliveryInfoDTO.getRecipientName() != null && !deliveryInfoDTO.getRecipientName().isBlank() &&
        //         deliveryInfoDTO.getAddress() != null && !deliveryInfoDTO.getAddress().isBlank();
        return true;
    }

    @Override
    public OrderDTO setDeliveryInfo(Long orderId, DeliveryInfoDTO deliveryInfoDTO) {
        if (!validateDeliveryInfo(deliveryInfoDTO)) {
            throw new IllegalArgumentException("Invalid delivery info");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setDeliveryInfo(deliveryInfoMapper.toEntity(deliveryInfoDTO));
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(savedOrder);
    }

    @Override
    public Boolean isRushOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> "RUSH_PLACED".equals(order.getOrderStatus()))
                .orElse(false);
    }

    @Override
    public Boolean checkAvailability(CartDTO cartDTO) {
        return cartService.checkAvailability(cartDTO);
    }

    @Override
    public void setPending(Long orderId) {
        orderRepository.updateOrderStatus(orderId, OrderStatus.PENDING);
    }

    @Override
    public Boolean checkRushDeliveryAddress(String address) {
        return true; // TODO: Implement address validation logic
    }

    @Override
    public Boolean checkCancellationValidity(Long orderId) {
        return true; // TODO: Implement cancellation validation logic
    }

    @Override
    public Boolean checkRushProductEligibility(Long productId) {
        // return cartService.checkRushEligibility(userId); // Delegate to CartService
        return true; // TODO: Implement rush eligibility check
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
        orderRepository.updatePaymentStatus(orderId, paymentStatus);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        orderRepository.updateOrderStatus(orderId, orderStatus);
    }

    @Override
    public List<OrderDTO> getOrderHistory(Integer customerId) {
        return orderRepository.findByUser_Id(customerId).stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }
}
