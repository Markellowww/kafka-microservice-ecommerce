package com.markellowww.ingestion.models;

import com.markellowww.ingestion.enums.OrderStatus;
import com.markellowww.ingestion.enums.ShippingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "orders")
public class Order {
    @Id
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