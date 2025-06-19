package com.darian.ecommerce.order.mapper;

import com.darian.ecommerce.order.dto.DeliveryInfoDTO;
import com.darian.ecommerce.order.entity.DeliveryInfo;
import org.springframework.stereotype.Component;

@Component
public class DeliveryInfoMapper {
    public  DeliveryInfoDTO toDTO(DeliveryInfo info) {
        return DeliveryInfoDTO.builder()
                .recipientName(info.getRecipientName())
                .phoneNumber(info.getPhoneNumber())
                .email(info.getEmail())
                .provinceCity(info.getProvinceCity())
                .address(info.getAddress())
                .shippingInstructions(info.getShippingInstructions())
                .build();
    }

    public  DeliveryInfo toEntity(DeliveryInfoDTO dto) {
        return DeliveryInfo.builder()
                .recipientName(dto.getRecipientName())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .provinceCity(dto.getProvinceCity())
                .address(dto.getAddress())
                .shippingInstructions(dto.getShippingInstructions())
                .build();
    }
}
