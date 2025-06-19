package com.darian.ecommerce.payment.dto;

import com.darian.ecommerce.payment.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundResult extends BasePaymentResult{
    private RefundStatus refundStatus;
    private LocalDateTime refundDate;
}
