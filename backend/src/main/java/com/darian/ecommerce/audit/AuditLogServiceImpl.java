package com.darian.ecommerce.audit;

import com.darian.ecommerce.payment.dto.BasePaymentResult;
import com.darian.ecommerce.audit.entity.AuditLog;
import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.audit.enums.ActionType;
import com.darian.ecommerce.payment.enums.TransactionType;
import com.darian.ecommerce.auth.enums.UserRole;
import com.darian.ecommerce.product.service.ProductService;
import com.darian.ecommerce.auth.UserService;
import com.darian.ecommerce.shared.constants.LoggerMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final AuditLogRepository auditLogRepository;
    private final UserService userService;
    private final ProductService productService;

    // Constructor for dependency injection
    public AuditLogServiceImpl(AuditLogRepository auditLogRepository,
                               UserService userService,
                               @Lazy ProductService productService) {
        this.auditLogRepository = auditLogRepository;
        this.userService = userService;
        this.productService = productService;
    }

    // Log a search action performed by a user
    @Override
    public void logSearchAction(Integer userId, String keyword, UserRole role) {
        log.info(LoggerMessages.PRODUCT_SEARCH, userId, role, keyword);
        User user = userService.getUserById(userId);
        
        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setRole(role);
        auditLog.setActionType(ActionType.SEARCH_PRODUCTS);
        auditLog.setKeyword(keyword);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    @Override
    public void logViewProduct(Long productId, Integer userId, UserRole role) {
        log.info(LoggerMessages.PRODUCT_VIEW, userId, role, productId);
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        
        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setRole(role);
        auditLog.setActionType(ActionType.VIEW_PRODUCT);
        auditLog.setProduct(product);
        auditLog.setKeyword(product.getName());
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    // Log an add product action, return true if successful
    @Override
    public Boolean logAddAction(Integer userId, Long productId, UserRole role) {
        try {
            log.info(LoggerMessages.PRODUCT_ADD, userId, role, productId);
            User user = userService.getUserById(userId);
            Product product = productService.getProductById(productId);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setUser(user);
            auditLog.setRole(role);
            auditLog.setActionType(ActionType.ADD_PRODUCT);
            auditLog.setProduct(product);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
            return true;
        } catch (Exception e) {
            log.error(LoggerMessages.AUDIT_ACTION_FAILED, "add", userId, e.getMessage());
            return false;
        }
    }

    // Log a delete product action, return true if successful
    @Override
    public Boolean logDeleteAction(Integer userId, Long productId, UserRole role) {
        try {
            log.info(LoggerMessages.PRODUCT_DELETE, userId, role, productId);
            User user = userService.getUserById(userId);
            Product product = productService.getProductById(productId);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setUser(user);
            auditLog.setRole(role);
            auditLog.setActionType(ActionType.DELETE_PRODUCT);
            auditLog.setProduct(product);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
            return true;
        } catch (Exception e) {
            log.error(LoggerMessages.AUDIT_ACTION_FAILED, "delete", userId, e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean checkDeleteLimit(Integer userId) {
        log.info(LoggerMessages.PRODUCT_DELETE_LIMIT, userId);
        Integer deleteCount = countDeletesByUserId(userId);
        boolean withinLimit = deleteCount < 30;
        log.info(LoggerMessages.PRODUCT_DELETE_COUNT, userId, deleteCount, withinLimit);
        return withinLimit;
    }
//
//    @Override
//    public Integer countDeletesByUserId(Integer userId) {
//        return auditLogRepository.countDeletesByUserId(userId);
//    }
    @Override
    //TODO: refactor this method
    public Integer countDeletesByUserId(Integer userId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return auditLogRepository.countByUserAndActionTypeAndTimestampAfter(
                userService.getUserById(userId), ActionType.DELETE_PRODUCT, startOfDay);
    }

    // Log an update product action, return true if successful
    @Override
    public Boolean logUpdateAction(Integer userId, Long productId, UserRole role) {
        try {
            log.info(LoggerMessages.PRODUCT_UPDATE, userId, role, productId);
            User user = userService.getUserById(userId);
            Product product = productService.getProductById(productId);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setUser(user);
            auditLog.setRole(role);
            auditLog.setActionType(ActionType.UPDATE_PRODUCT);
            auditLog.setProduct(product);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
            return true;
        } catch (Exception e) {
            log.error(LoggerMessages.AUDIT_ACTION_FAILED, "update", userId, e.getMessage());
            return false;
        }
    }

    // Log an order-related action, return true if successful
    @Override
    public Boolean logOrderAction(Integer userId, Long orderId, UserRole role, ActionType action) {
        try {
            log.info(LoggerMessages.AUDIT_ORDER_ACTION, userId, role, orderId);
            User user = userService.getUserById(userId);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setUser(user);
            auditLog.setRole(role);
            auditLog.setActionType(action);
            auditLog.setKeyword("Order ID: " + orderId);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
            return true;
        } catch (Exception e) {
            log.error(LoggerMessages.AUDIT_ACTION_FAILED, "order", userId, e.getMessage());
            return false;
        }
    }

    // Log a payment transaction
    @Override
    public void logPayment(BasePaymentResult result) {
        log.info(LoggerMessages.AUDIT_PAYMENT, result.getTransactionId(), result.getTransactionType());
        AuditLog auditLog = new AuditLog();
        auditLog.setActionType(result.getTransactionType() == TransactionType.PAYMENT ? 
                ActionType.PAY_ORDER : ActionType.CANCEL_ORDER);
        auditLog.setKeyword(String.format("%s processed for order %d (Transaction ID: %s)", 
                result.getTransactionType(), result.getOrderId(), result.getTransactionId()));
        auditLog.setRole(UserRole.CUSTOMER);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }
}
