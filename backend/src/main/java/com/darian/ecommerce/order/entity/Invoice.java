package com.darian.ecommerce.order.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//nay ko phai la mot entity duoc luu trong db
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    private Long orderId;
    private Float amount;
    private String content;
}
