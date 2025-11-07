package com.markellowww.kafgen.models;

import com.markellowww.kafgen.enums.OrderStatus;
import com.markellowww.kafgen.enums.ShippingType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @author Markelloww
 */

@Builder
public class Order {
    private String orderId;
    private String customerId;
    private OrderStatus status;
    private ShippingType shippingType;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private Address deliveryAddress;
    private Instant timestamp;
    private Instant processedAt;

    // Только для выходного события
//    private String trackingNumber;
//    private String assignedWarehouse;
//    private Instant estimatedDelivery;
}