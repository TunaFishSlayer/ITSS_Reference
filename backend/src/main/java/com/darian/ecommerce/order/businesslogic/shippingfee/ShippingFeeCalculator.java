package com.darian.ecommerce.order.businesslogic.shippingfee;

import com.darian.ecommerce.order.dto.BaseOrderDTO;

public interface ShippingFeeCalculator {
    // Calculate shipping fee based on order details
    Float calculateShippingFee(BaseOrderDTO dto);
}
