package com.darian.ecommerce.payment;

import com.darian.ecommerce.cart.CartItemService;
import com.darian.ecommerce.order.entity.OrderItem;
import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.payment.dto.PaymentConfirmDTO;
import com.darian.ecommerce.payment.entity.PaymentTransaction;
import com.darian.ecommerce.payment.enums.RefundStatus;
import com.darian.ecommerce.payment.exception.PaymentException;
import com.darian.ecommerce.payment.dto.PaymentResult;
import com.darian.ecommerce.payment.dto.RefundResult;
import com.darian.ecommerce.order.entity.Order;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentMethod;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.payment.exception.PaymentValidationException;
import com.darian.ecommerce.payment.factory.PaymentStrategyFactory;
import com.darian.ecommerce.audit.AuditLogService;
import com.darian.ecommerce.order.OrderService;
import com.darian.ecommerce.payment.mapper.PaymentTransactionMapper;
import com.darian.ecommerce.payment.mapper.VNPayIpnMapper;
import com.darian.ecommerce.product.service.ProductService;
import com.darian.ecommerce.shared.exception.ErrorCode;
import com.darian.ecommerce.subsystem.vnpay.VNPaySubsystemService;
import com.darian.ecommerce.shared.constants.ErrorMessages;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    // Cohesion: Functional Cohesion
    // → Lớp này tập trung xử lý logic thanh toán và hoàn tiền (gọi subsystem, cập nhật trạng thái đơn hàng, log, validate...).

    // SRP: Vi phạm nhẹ
    // → Dù các method đều liên quan đến payment, nhưng gồm cả `validate`, `checkCancellationValidity`, `refund`, `pay`
    // → có thể tách nhỏ thành PaymentProcessor, RefundProcessor nếu phức tạp tăng lên.
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentTransactionRepository paymentRepository;
    private final VNPaySubsystemService vnpayService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final AuditLogService auditLogService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    // Constructor injection for dependencies
    public PaymentServiceImpl(PaymentStrategyFactory paymentStrategyFactory, PaymentTransactionRepository paymentRepository,
                              VNPaySubsystemService vnpayService,
                              OrderService orderService,
                              ProductService productService,
                              CartItemService cartItemService,
                              AuditLogService auditLogService,
                              PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentRepository = paymentRepository;
        this.vnpayService = vnpayService;
        this.orderService = orderService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.auditLogService = auditLogService;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

//    @Override
//    public PaymentResult payOrder(Long orderId, String paymentMethod, HttpServletRequest request) throws OrderNotFoundException {
//        log.info(LoggerMessages.PAYMENT_PROCESSING, orderId, paymentMethod);
//
//        Order order = orderService.findOrderById(orderId)
//                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
//
//        Float total = order.getTotal();
//        if (!validatePayment(order)) {
//            log.error(LoggerMessages.PAYMENT_VALIDATION_FAILED, orderId);
//            throw new PaymentException(String.format(ErrorMessages.PAYMENT_INVALID_STATUS, orderId));
//        }
//
//
//        PaymentResult result = new PaymentResult() ;
//        if (PaymentMethod.VNPAY.name().equalsIgnoreCase(paymentMethod)){
//            result = vnpayService.processPayment(orderId, total, "Payment for order " + orderId, request);
//        }
//        else {
//            PaymentStrategy strategy = paymentStrategyFactory.createPaymentStrategy(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
//            PaymentResDTO resultdto = new PaymentResDTO();
//            PaymentResult paymentResult = strategy.processPayment(orderId, total,"Payment for order " + orderId, request );
//            resultdto.setStatus(paymentResult.getPaymentStatus().toString());
//            resultdto.setMessage("Payment processed");
//        }
//
//        auditLogService.logPayment(result);
//        return new PaymentResult();
//    }

    // ----------------------------------------------------------------------------------------------------------------------------

    @Override
    public String payOrder(Long orderId, String paymentMethod, HttpServletRequest request) throws OrderNotFoundException, UnsupportedEncodingException {
        log.info(LoggerMessages.PAYMENT_PROCESSING, orderId, paymentMethod);

        Order order = orderService.findOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));

        log.info(order.getTotal()+" , "+order.getDiscount()+" , " + order.getSubtotal()+" , " + order.getShippingFee()+" , " + order.getVAT());
        Float total = order.getTotal();
        if (!validatePayment(order)) {
            log.error(LoggerMessages.PAYMENT_VALIDATION_FAILED, orderId);
            throw new PaymentValidationException(orderId.toString());
        }

        PaymentResult paymentResult;
        if (PaymentMethod.VNPAY.name().equalsIgnoreCase(paymentMethod)){
            paymentResult = vnpayService.processPayment(orderId, total,request);
        }
        else {
            PaymentStrategy strategy = paymentStrategyFactory.createPaymentStrategy(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
            paymentResult = strategy.processPayment(orderId, total, request );
        }
        return paymentResult.getReturnUrl();
    }

    @Override
    public Boolean validatePayment(Order order) throws OrderNotFoundException {
        Long orderId = order.getOrderId();
        OrderStatus orderStatus = order.getOrderStatus();
        PaymentStatus paymentStatus = order.getPaymentStatus();

        log.info(LoggerMessages.VALIDATION_STARTED, "payment for order " + orderId);

        if (orderStatus == OrderStatus.PENDING) {
            orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
        }
        else if (orderStatus != OrderStatus.CONFIRMED) {
            log.warn(LoggerMessages.VALIDATION_FAILED, "order status",
                String.format(ErrorMessages.ORDER_INVALID_STATUS, orderStatus));
            return false;
        }

        if (paymentStatus != PaymentStatus.UNPAID) {
            log.warn(LoggerMessages.VALIDATION_FAILED, "payment status",
                String.format(ErrorMessages.PAYMENT_ALREADY_PROCESSED, orderId));
            return false;
        }

        log.info(LoggerMessages.VALIDATION_COMPLETED, "payment validation", true);
        return true;
    }


    @Override
    @Transactional
    public boolean handlePayment(PaymentConfirmDTO dto) {
        Long orderId = Long.valueOf(dto.getVnpTxnRef());
        if ("00".equals(dto.getVnpResponseCode())) {
            // Lấy order từ txnRef
            Order order = orderService.findOrderById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));
            PaymentTransaction transaction = PaymentTransactionMapper.toEntity(dto, order, PaymentMethod.VNPAY);

            //save payment transaction
            paymentTransactionRepository.save(transaction); // hoặc createOrUpdate

            // edit product in the order 's quantity
            List<OrderItem> orderItems = order.getItems();
            Integer userId = order.getUser().getId();
            for (OrderItem orderItem : orderItems){

                Long productId = orderItem.getProduct().getProductId();

                //update product quantity
                Integer productQuantity = orderItem.getQuantity();
                productService.reduceProductQuantity( productId, productQuantity);

                //remove from cart
//                cartItemService.removeFromCart( userId,productId );
            }

            //update order
            orderService.updatePaymentStatus(orderId, PaymentStatus.PAID);

            //log payment
            PaymentResult result = PaymentTransactionMapper.toPaymentResult(transaction);
            auditLogService.logPayment(result);

            return true;

        } else {
            // TODO: xử lý thất bại nếu cần
            log.warn("Giao dịch thất bại hoặc bị hủy: {}", dto.getVnpTxnRef());
            throw new PaymentException(ErrorCode.PAYMENT_FAILED, "Thanh toán thất bại cho orderId: " + orderId);
        }
    }



    // ----------------------------------------------------------------------------------------------------------------------------

    @Override
    public RefundResult processRefund(Long orderId) {
        log.info(LoggerMessages.REFUND_PROCESSING, orderId, "refund");
        if (!checkCancellationValidity(orderId)) {
            log.error(LoggerMessages.VALIDATION_FAILED, "refund", orderId);
            throw new PaymentException( ErrorCode.ORDER_CANNOT_BE_MODIFIED , ErrorCode.ORDER_CANNOT_BE_MODIFIED.format(orderId) );
        }
        RefundResult result = vnpayService.processRefund(orderId);
        if (RefundStatus.NOT_REQUESTED.equals(result.getRefundStatus())) {
            orderService.updatePaymentStatus(orderId, PaymentStatus.REFUNDED);
            log.info(LoggerMessages.REFUND_COMPLETED, orderId, result.getRefundStatus());
        } else {
            log.warn(LoggerMessages.REFUND_FAILED, orderId, result.getErrorMessage());
        }
        auditLogService.logPayment(result);
        return result;
    }

    @Override
    public Boolean checkCancellationValidity(Long orderId) {
        log.info(LoggerMessages.VALIDATION_STARTED, "cancellation for order " + orderId);
        return orderService.checkCancellationValidity(orderId);
    }
//
//    @Override
//    public boolean handleVnPayIpn(Map<String, String> vnpParams) {
//        log.info(LoggerMessages.VNPAY_IPN_PROCESSING, vnpParams.get("vnp_TxnRef"));
////        TODO
////        PaymentResult result = vnPaySubsystem.handleIpnCallback(vnpParams);
//        PaymentResult result = new PaymentResult();
//        if ("PAID".equals(result.getPaymentStatus())) {
//            orderService.updatePaymentStatus(Long.valueOf(vnpParams.get("vnp_TxnRef")), PaymentStatus.PAID);
//            auditLogService.logPayment(result);
//            return true;
//        }
//        return false;
//    }

    @Override
    @Transactional
    public boolean handleVnPayIpn(Map<String, String> vnpParams) {
        PaymentConfirmDTO dto = VNPayIpnMapper.toDTO(vnpParams);

        String txnRef = dto.getVnpTxnRef();
        String responseCode = dto.getVnpResponseCode();
        String transactionNo = dto.getVnpTransactionNo();

        log.info(LoggerMessages.VNPAY_IPN_PROCESSING, txnRef);

        Long orderId = Long.valueOf(txnRef);
        if (!"00".equals(responseCode)) {
            log.warn("IPN - Thanh toán thất bại cho orderId: {}, responseCode={}", orderId, responseCode);
            throw new PaymentException(ErrorCode.PAYMENT_FAILED, "IPN xác nhận thất bại cho orderId: " + orderId);
        }

        // Check nếu order tồn tại
        Order order = orderService.findOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId)));

        // Kiểm tra nếu đã ghi nhận payment rồi thì bỏ qua
        Optional<PaymentTransaction> existing = paymentTransactionRepository.findByOrder_OrderId(orderId);
        if (existing.isPresent()) {
            log.info("IPN - Giao dịch đã được ghi nhận trước đó cho orderId: {}", orderId);
            return true; // idempotent
        }

        // save payment
        PaymentTransaction transaction = PaymentTransactionMapper.toEntity(dto, order, PaymentMethod.VNPAY);
        paymentTransactionRepository.save(transaction);


        // Cập nhật order
        orderService.updatePaymentStatus(orderId, PaymentStatus.PAID);

        // edit product in the order 's quantity
        List<OrderItem> orderItems = order.getItems();
        for (OrderItem orderItem : orderItems){
            Long productId = orderItem.getProduct().getProductId();
            Integer productQuantity = orderItem.getQuantity();
            productService.reduceProductQuantity( productId, productQuantity);
        }

        // Log lại
        PaymentResult result = PaymentTransactionMapper.toPaymentResult(transaction);
        auditLogService.logPayment(result);

        //clear cart
        cartItemService.clearCart(order.getUser().getId());

        return true;
    }
}
