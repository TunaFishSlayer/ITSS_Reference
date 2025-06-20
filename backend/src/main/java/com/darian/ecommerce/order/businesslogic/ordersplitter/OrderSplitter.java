package com.darian.ecommerce.order.businesslogic.ordersplitter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.darian.ecommerce.order.dto.BaseOrderDTO;
import com.darian.ecommerce.order.dto.OrderItemDTO;
import com.darian.ecommerce.order.dto.RushOrderDTO;
import com.darian.ecommerce.order.dto.SplitOrderDTO;
import com.darian.ecommerce.order.enums.OrderStatus;

@Component
public class OrderSplitter {

    public SplitOrderDTO splitOrder(BaseOrderDTO baseOrderDTO) {
        List<OrderItemDTO> rushItems = getRushEligibleItems(baseOrderDTO.getItems());
        List<OrderItemDTO> standardItems = getStandardDeliveryItems(baseOrderDTO.getItems());

        RushOrderDTO rushOrder = null;
        BaseOrderDTO standardOrder = null;

        if (!rushItems.isEmpty()) {
            rushOrder = cloneRushOrder(baseOrderDTO, rushItems);
        }

        if (!standardItems.isEmpty()) {
            standardOrder = cloneStandardOrder(baseOrderDTO, standardItems);
        }

        return new SplitOrderDTO(rushOrder, standardOrder);
    }

    private List<OrderItemDTO> getRushEligibleItems(List<OrderItemDTO> items) {
        return items.stream()
                .filter(OrderItemDTO::isRushEligible)
                .collect(Collectors.toList());
    }

    private List<OrderItemDTO> getStandardDeliveryItems(List<OrderItemDTO> items) {
        return items.stream()
                .filter(item -> !item.isRushEligible())
                .collect(Collectors.toList());
    }

    private RushOrderDTO cloneRushOrder(BaseOrderDTO original, List<OrderItemDTO> items) {
        RushOrderDTO order = new RushOrderDTO();
        // order.setOrderId(...) REMOVED: Let DB generate the ID
        order.setCustomerId(original.getCustomerId());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDeliveryInfo(original.getDeliveryInfo());
        order.setCreatedDate(original.getCreatedDate());
        order.setItems(items); // Add this line to set rush items
        order.setRushDeliveryTime(LocalDateTime.now().plusHours(2));
        return order;
    }

    private BaseOrderDTO cloneStandardOrder(BaseOrderDTO original, List<OrderItemDTO> items) {
        BaseOrderDTO order = new BaseOrderDTO();
        // order.setOrderId(...) REMOVED: Let DB generate the ID
        order.setCustomerId(original.getCustomerId());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDeliveryInfo(original.getDeliveryInfo());
        order.setItems(items);
        order.setCreatedDate(original.getCreatedDate());
        return order;
    }


}
