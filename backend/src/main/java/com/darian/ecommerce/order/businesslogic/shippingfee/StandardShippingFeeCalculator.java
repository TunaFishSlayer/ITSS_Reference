package com.darian.ecommerce.order.businesslogic.shippingfee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.darian.ecommerce.order.dto.BaseOrderDTO;
import com.darian.ecommerce.order.dto.OrderItemDTO;

@Component
public class StandardShippingFeeCalculator implements ShippingFeeCalculator {
    private static final BigDecimal INNER_CITY_INITIAL_FEE = new BigDecimal("22000");
    private static final BigDecimal OTHER_LOCATION_INITIAL_FEE = new BigDecimal("30000");
    private static final BigDecimal ADDITIONAL_FEE_PER_HALF_KG = new BigDecimal("2500");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("100000");
    private static final BigDecimal MAX_FREE_SHIPPING_AMOUNT = new BigDecimal("25000");
    private static final BigDecimal INNER_CITY_INITIAL_WEIGHT_KG = new BigDecimal("3.0");
    private static final BigDecimal OTHER_LOCATION_INITIAL_WEIGHT_KG = new BigDecimal("0.5");
    private static final BigDecimal HALF_KG = new BigDecimal("0.5");

    private static final Set<String> INNER_CITY_PROVINCES = new HashSet<>(Arrays.asList(
            "hanoi", "ho chi minh", "hcmc", "saigon"
    ));

    @Override
    public Float calculateShippingFee(BaseOrderDTO order) {
        if (order == null || order.getItems() == null || order.getDeliveryInfo() == null) {
            return 0.0f;
        }

        BigDecimal orderValue = calculateOrderValue(order.getItems());

        BigDecimal heaviestItemWeight = findHeaviestItemWeight(order.getItems());
        boolean isInnerCity = isInnerCityProvince(order.getDeliveryInfo().getProvinceCity());

        BigDecimal fee;
        if (isInnerCity) {
            fee = INNER_CITY_INITIAL_FEE;
            if (heaviestItemWeight.compareTo(INNER_CITY_INITIAL_WEIGHT_KG) > 0) {
                BigDecimal additionalWeight = heaviestItemWeight.subtract(INNER_CITY_INITIAL_WEIGHT_KG);
                BigDecimal additionalHalfKgs = additionalWeight.divide(HALF_KG, 0, RoundingMode.CEILING);
                fee = fee.add(additionalHalfKgs.multiply(ADDITIONAL_FEE_PER_HALF_KG));
            }
        } else {
            fee = OTHER_LOCATION_INITIAL_FEE;
            if (heaviestItemWeight.compareTo(OTHER_LOCATION_INITIAL_WEIGHT_KG) > 0) {
                BigDecimal additionalWeight = heaviestItemWeight.subtract(OTHER_LOCATION_INITIAL_WEIGHT_KG);
                BigDecimal additionalHalfKgs = additionalWeight.divide(HALF_KG, 0, RoundingMode.CEILING);
                fee = fee.add(additionalHalfKgs.multiply(ADDITIONAL_FEE_PER_HALF_KG));
            }
        }

        if (orderValue.compareTo(FREE_SHIPPING_THRESHOLD) > 0 && fee.compareTo(MAX_FREE_SHIPPING_AMOUNT) <= 0) {
            return 0.0f;
        }

        return fee.floatValue();
    }

    protected BigDecimal calculateOrderValue(List<OrderItemDTO> items) {
        return items.stream()
            .map(item -> new BigDecimal(Float.toString(item.getLineTotal())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected BigDecimal findHeaviestItemWeight(List<OrderItemDTO> items) {
    if (items == null || items.isEmpty()) {
        return BigDecimal.ZERO;
    }

    return items.stream()
        .map(item -> new BigDecimal(Float.toString(item.getLineWeight())))
        .max(BigDecimal::compareTo)
        .orElse(BigDecimal.ZERO);
    }

    protected boolean isInnerCityProvince(String provinceCity) {
        if (provinceCity == null) {
            return false;
        }
        return INNER_CITY_PROVINCES.contains(provinceCity.toLowerCase().trim());
    }
}
