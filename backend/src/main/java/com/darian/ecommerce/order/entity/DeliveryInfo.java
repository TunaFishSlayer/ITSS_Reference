package com.darian.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_info")
public class DeliveryInfo {
    // Primary key
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_info_id")
    private Long id;

    // Name of the recipient
    @Column(name = "recipient_name")
    private String recipientName;

    // Phone number of the recipient
    @Column(name = "phone_number")
    private String phoneNumber;

    // Email of the recipient
    private String email;

    // Province or city for delivery
    @Column(name = "province_city")
    private String provinceCity;

    // Detailed address for delivery
    private String address;

    // Additional shipping instructions
    @Column(name = "shipping_instruction")
    private String shippingInstructions;

}
