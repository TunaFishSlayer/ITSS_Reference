package com.darian.ecommerce.order.businesslogic.shippingfee;

import com.darian.ecommerce.order.dto.BaseOrderDTO;
import com.darian.ecommerce.order.dto.RushOrderDTO;
import org.springframework.stereotype.Component;

@Component
public class ShippingFeeCalculatorFactory {
    // Get the appropriate calculator based on order type
    public ShippingFeeCalculator getCalculator(BaseOrderDTO dto) {
        if (dto instanceof RushOrderDTO) {
            return new RushShippingFeeCalculator();
        }
        return new StandardShippingFeeCalculator();
    }
}
