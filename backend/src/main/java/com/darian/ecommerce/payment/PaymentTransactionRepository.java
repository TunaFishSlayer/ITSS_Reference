package com.darian.ecommerce.payment;

import com.darian.ecommerce.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    // Cohesion: Functional Cohesion
    // → Chỉ xử lý các truy vấn dữ liệu liên quan đến giao dịch thanh toán.

    // SRP: Không vi phạm
    // → Không xử lý logic nghiệp vụ, chỉ thao tác với database.

    public PaymentTransaction save(PaymentTransaction payment);

    public Optional<PaymentTransaction> findByOrder_OrderId(Long orderId);

    public PaymentTransaction findByTransactionCode(String transactionId);
}
