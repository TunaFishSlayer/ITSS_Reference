package com.darian.ecommerce.audit;

import com.darian.ecommerce.payment.dto.BasePaymentResult;
import com.darian.ecommerce.audit.enums.ActionType;
import com.darian.ecommerce.auth.enums.UserRole;

public interface AuditLogService {

    // Log a search action performed by a user
    void logSearchAction(Integer userId, String keyword, UserRole role);

    // Log a product view action by a user
    void logViewProduct(Long productId, Integer userId, UserRole role);

    // Log an add product action, return true if successful
    Boolean logAddAction(Integer userId, Long productId, UserRole role);

    // Log a delete product action, return true if successful
    Boolean logDeleteAction(Integer userId, Long productId, UserRole role);

    // Check if the user has exceeded the delete limit
    Boolean checkDeleteLimit(Integer userId);

    //count delete by specific user
    Integer countDeletesByUserId(Integer userId);

    // Log an update product action, return true if successful
    Boolean logUpdateAction(Integer userId, Long productId, UserRole role);

    // Log an order-related action, return true if successful
    Boolean logOrderAction(Integer userId, Long orderId, UserRole role, ActionType action);

    // Log a payment transaction
    void logPayment(BasePaymentResult paymentResult);

    //TODO: log refund transaction
    // void logRefund(BasePaymentResult refundResult);

}
