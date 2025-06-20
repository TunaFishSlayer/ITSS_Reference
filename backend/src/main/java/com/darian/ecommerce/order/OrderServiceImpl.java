package com.darian.ecommerce.order;

import com.darian.ecommerce.audit.AuditLogService;
import com.darian.ecommerce.auth.UserService;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.order.mapper.DeliveryInfoMapper;
import com.darian.ecommerce.order.mapper.OrderMapper;
import com.darian.ecommerce.order.businesslogic.ordersplitter.OrderSplitter;
import com.darian.ecommerce.order.businesslogic.shippingfee.ShippingFeeCalculatorFactory;
import com.darian.ecommerce.cart.CartService;
import com.darian.ecommerce.cart.dto.CartDTO;
import com.darian.ecommerce.order.dto.RushOrderDeliveryInfoDTO;
import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.audit.enums.ActionType;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.auth.enums.UserRole;
import com.darian.ecommerce.order.entity.DeliveryInfo;
import com.darian.ecommerce.order.dto.BaseOrderDTO;
import com.darian.ecommerce.order.dto.DeliveryInfoDTO;
import com.darian.ecommerce.order.dto.InvoiceDTO;
import com.darian.ecommerce.order.dto.OrderDTO;
import com.darian.ecommerce.order.dto.RushOrderDTO;
import com.darian.ecommerce.order.dto.SplitOrderDTO;
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
    private final OrderSplitter orderSplitter;
    public OrderServiceImpl(OrderRepository orderRepository,
                          ShippingFeeCalculatorFactory calculatorFactory,
                          CartService cartService,
                          UserService userService,
                          AuditLogService auditLogService,
                          OrderMapper orderMapper,
                          DeliveryInfoMapper deliveryInfoMapper,
                          com.darian.ecommerce.order.businesslogic.ordersplitter.OrderSplitter orderSplitter) {
        this.orderRepository = orderRepository;
        this.calculatorFactory = calculatorFactory;
        this.cartService = cartService;
        this.userService = userService;
        this.auditLogService = auditLogService;
        this.orderMapper = orderMapper;
        this.deliveryInfoMapper = deliveryInfoMapper;
        this.orderSplitter = orderSplitter;
    }

    @Override
    public OrderDTO createOrder(CartDTO cartDTO) {
        if (!checkAvailability(cartDTO)) {
            throw new IllegalStateException(String.format(ErrorMessages.VALIDATION_FAILED, "cart items not available"));
        }
        Order order = new Order();
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
    public SplitOrderDTO placeOrder(OrderDTO orderDTO) {
        if (orderDTO == null || orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or not provided.");
        }

        if (!validateDeliveryInfo(orderDTO.getDeliveryInfo())) {
            throw new IllegalArgumentException("Invalid or incomplete delivery information.");
        }

        SplitOrderDTO splitResult = orderSplitter.splitOrder(orderDTO);

        RushOrderDTO rushOrderResult = null;
        BaseOrderDTO standardOrderResult = null;

        if (splitResult.rushOrder() != null) {
            RushOrderDTO rushOrderDTO = splitResult.rushOrder();

            if (!isRushDeliverySupported(rushOrderDTO)) {
                throw new IllegalStateException("Rush delivery not supported for this address or products.");
            }

            Order rushOrder = orderMapper.toEntity(rushOrderDTO, true);
            rushOrder.setShippingFee(calculatorFactory.getCalculator(rushOrderDTO).calculateShippingFee(rushOrderDTO));
            rushOrder.setOrderStatus(OrderStatus.PENDING);
            Order savedRushOrder = orderRepository.save(rushOrder);

            logger.info(LoggerMessages.ORDER_CREATED, savedRushOrder.getOrderId());
            auditLogService.logOrderAction(savedRushOrder.getUser().getId(), savedRushOrder.getOrderId(), UserRole.CUSTOMER, ActionType.PLACE_ORDER);

            rushOrderResult = orderMapper.toRushOrderDTO(savedRushOrder);
        }

        if (splitResult.standardOrder() != null) {
            BaseOrderDTO standardOrderDTO = splitResult.standardOrder();
            Order standardOrder = orderMapper.toEntity(standardOrderDTO, false);
            standardOrder.setShippingFee(calculatorFactory.getCalculator(standardOrderDTO).calculateShippingFee(standardOrderDTO));
            standardOrder.setOrderStatus(OrderStatus.PENDING);
            Order savedStandardOrder = orderRepository.save(standardOrder);

            logger.info(LoggerMessages.ORDER_CREATED, savedStandardOrder.getOrderId());
            auditLogService.logOrderAction(savedStandardOrder.getUser().getId(), savedStandardOrder.getOrderId(), UserRole.CUSTOMER, ActionType.PLACE_ORDER);

            standardOrderResult = orderMapper.toBaseOrderDTO(savedStandardOrder);
        }

        return new SplitOrderDTO(rushOrderResult, standardOrderResult);
    }

    /*@Override
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
    }*/

    @Override
    public InvoiceDTO getInvoice(Long orderId) throws OrderNotFoundException {
        Order order = findOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setOrderId(orderId);
        invoice.setShippingFee(order.getShippingFee());
        invoice.setTotal(order.getTotal());
        return invoice;
    }

    @Override
    public void cancelOrder(Long orderId) throws OrderNotFoundException {
        Order order = findOrderById(orderId)
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
        return deliveryInfoDTO != null
            && deliveryInfoDTO.getRecipientName() != null
            && !deliveryInfoDTO.getRecipientName().isBlank()
            && deliveryInfoDTO.getAddress() != null
            && !deliveryInfoDTO.getAddress().isBlank()
            && deliveryInfoDTO.getProvinceCity() != null
            && !deliveryInfoDTO.getProvinceCity().isBlank();
    }

    @Override
    public OrderDTO setDeliveryInfo(Long orderId, DeliveryInfoDTO deliveryInfoDTO) {
        if (!validateDeliveryInfo(deliveryInfoDTO)) {
            throw new IllegalArgumentException("Invalid delivery info");
        }
        DeliveryInfo deliveryInfo = deliveryInfoMapper.toEntity(deliveryInfoDTO);
        orderRepository.updateDeliveryInfo(orderId, deliveryInfo);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    public RushOrderDTO setRushDeliveryInfo(Long orderId, RushOrderDeliveryInfoDTO rushOrderUpdateDTO) {
        Order order = findOrderById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));

        if (!Boolean.TRUE.equals(order.getIsRushOrder())) {
            throw new IllegalStateException("Order is not a rush order.");
        }

        // Update rush delivery time and instructions
        if (order.getDeliveryInfo() != null) {
            order.getDeliveryInfo().setDeliveryInstructions(rushOrderUpdateDTO.getDeliveryInstruction());
            orderRepository.updateDeliveryInfo(orderId, order.getDeliveryInfo());
        }
        order.setRushDeliveryTime(rushOrderUpdateDTO.getRushDeliveryTime());
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toRushOrderDTO(savedOrder);
    }

    @Override
    public Boolean checkAvailability(CartDTO cartDTO) {
        return cartService.checkAvailability(cartDTO);
    }

    @Override
    public void setPending(Long orderId) {
        orderRepository.updateOrderStatus(orderId, OrderStatus.PENDING);
    }

    public void setRejected(Long orderId) {
        orderRepository.updateOrderStatus(orderId, OrderStatus.REJECTED);
    }

    public void setConfirmed(Long orderId) {
        orderRepository.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
    }

    /*@Override
    public Boolean checkRushDeliveryAddress(String address) {
        return true; 
    } */

    public List<BaseOrderDTO> getOrdersbyStatus(OrderStatus status) {
    return orderRepository.findAll().stream()
            .filter(order -> status == null || order.getOrderStatus() == status)
            .map(order -> {
                if (Boolean.TRUE.equals(order.getIsRushOrder())) {
                    return orderMapper.toRushOrderDTO(order);
                } else {
                    return orderMapper.toBaseOrderDTO(order);
                }
            })
            .collect(Collectors.toList());
    }

    @Override
    public Boolean checkCancellationValidity(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
        return order.getOrderStatus() == OrderStatus.PENDING;
    }

    @Override
    public Boolean isRushDeliverySupported(BaseOrderDTO baseOrderDTO) {
        // Check if address is in Hanoi inner city and all products are rush eligible
        return baseOrderDTO.getDeliveryInfo().getProvinceCity().equalsIgnoreCase("hanoi")
            && baseOrderDTO.getItems().stream().allMatch(item -> item.isRushEligible());
    }

    @Override
    public Optional<Order> findOrderById(Long orderId){
        return orderRepository.findById(orderId);
    }

    @Override
    public BaseOrderDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));

        if (Boolean.TRUE.equals(order.getIsRushOrder())) {
            return orderMapper.toRushOrderDTO(order); 
        } else {
            return orderMapper.toBaseOrderDTO(order);
        }
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
