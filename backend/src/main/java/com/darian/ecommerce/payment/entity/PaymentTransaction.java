package com.darian.ecommerce.payment.entity;

import com.darian.ecommerce.payment.enums.PaymentMethod;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.payment.enums.RefundStatus;
import com.darian.ecommerce.payment.enums.TransactionType;
import com.darian.ecommerce.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_transaction")
public class PaymentTransaction {
    // Cohesion: Functional Cohesion
    // → Class mô tả đầy đủ thông tin một giao dịch thanh toán, các field đều thống nhất về mục đích.

    // SRP: Không vi phạm
    // → Chỉ biểu diễn dữ liệu, không chứa logic nghiệp vụ.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer id;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "total_amount")
    private Float totalAmount;

    @Column(name = "transaction_content")
    private String transactionContent;

    @Column(name = "transaction_timestamp")
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @Column(name = "refund_timestamp")
    private LocalDateTime refundDate;

}
