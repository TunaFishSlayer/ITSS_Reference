package com.darian.ecommerce.order.businesslogic.shippingfee;

import com.darian.ecommerce.order.dto.BaseOrderDTO;

public class RushShippingFeeCalculator implements ShippingFeeCalculator{
    // Calculate rush shipping fee (example logic)
    @Override
    public Float calculateShippingFee(BaseOrderDTO dto) {
        // Higher base fee for rush delivery
        return 20f + (dto.getSubtotal() > 100 ? 10f : 0f);
    }
}
