package com.darian.ecommerce.order.businesslogic.shippingfee;

import com.darian.ecommerce.order.dto.BaseOrderDTO;
import com.darian.ecommerce.order.dto.OrderItemDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RushShippingFeeCalculator implements ShippingFeeCalculator {

    private static final BigDecimal INNER_CITY_INITIAL_FEE = new BigDecimal("22000");
    private static final BigDecimal INNER_CITY_INITIAL_WEIGHT_KG = new BigDecimal("3.0");
    private static final BigDecimal HALF_KG = new BigDecimal("0.5");
    private static final BigDecimal ADDITIONAL_FEE_PER_HALF_KG = new BigDecimal("2500");
    private static final BigDecimal RUSH_ADDITIONAL_FEE = new BigDecimal("10000");

    // Calculate rush shipping fee (example logic)
    @Override
    public Float calculateShippingFee(BaseOrderDTO dto) {
        if (dto == null || dto.getItems() == null || dto.getDeliveryInfo() == null) {
            return 0.0f;
        }

        if (!isHanoiInnerCity(dto.getDeliveryInfo().getProvinceCity())) {
            throw new IllegalArgumentException("Rush shipping is only available for Hanoi inner city.");
        }

        List<OrderItemDTO> rushItems = dto.getItems();
        BigDecimal heaviestItemWeight = findHeaviestItemWeight(rushItems);
        BigDecimal baseFee = INNER_CITY_INITIAL_FEE;

        if (heaviestItemWeight.compareTo(INNER_CITY_INITIAL_WEIGHT_KG) > 0) {
            BigDecimal additionalWeight = heaviestItemWeight.subtract(INNER_CITY_INITIAL_WEIGHT_KG);
            BigDecimal additionalHalfKgs = additionalWeight.divide(HALF_KG, 0, RoundingMode.CEILING);
            baseFee = baseFee.add(additionalHalfKgs.multiply(ADDITIONAL_FEE_PER_HALF_KG));
        }

        int totalRushQuantity = rushItems.stream().mapToInt(OrderItemDTO::getQuantity).sum();
        BigDecimal rushAdditionalFee = RUSH_ADDITIONAL_FEE.multiply(BigDecimal.valueOf(totalRushQuantity));

        BigDecimal totalFee = baseFee.add(rushAdditionalFee);
        return totalFee.floatValue();
    }

    private boolean isHanoiInnerCity(String provinceCity) {
        if (provinceCity == null) {
            return false;
        }
        return "hanoi".equalsIgnoreCase(provinceCity.trim());
    }

    private BigDecimal findHeaviestItemWeight(List<OrderItemDTO> items) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;

        return items.stream()
                .map(item -> new BigDecimal(Float.toString(item.getLineWeight())))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
