package com.markellowww.processing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.markellowww.processing.enums.OrderStatus;
import com.markellowww.processing.enums.ShippingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @author Markelloww
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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