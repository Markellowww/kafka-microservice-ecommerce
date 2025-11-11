package com.markellowww.kafgen.models;

import com.markellowww.kafgen.enums.OrderStatus;
import com.markellowww.kafgen.enums.ShippingType;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class Order {

    @Schema(
            description = "Unique order identifier",
            example = "ORD-12U41FPU",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String orderId;

    @Schema(
            description = "Unique client Identifier",
            example = "CUST-10IJ23",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String customerId;

    @Schema(
            description = "Order status",
            requiredMode = Schema.RequiredMode.REQUIRED,
            implementation = OrderStatus.class
    )
    private OrderStatus status;

    @Schema(
            description = "Shipping type",
            requiredMode = Schema.RequiredMode.REQUIRED,
            implementation = ShippingType.class
    )
    private ShippingType shippingType;

    @Schema(
            description = "List of items in the order",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<OrderItem> items;

    @Schema(
            description = "Total order amount",
            example = "299.99",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal totalAmount;

    @Schema(
            description = "Delivery address",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Address deliveryAddress;

    @Schema(
            description = "Order creation timestamp",
            example = "1550330233.134",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant timestamp;

    @Schema(
            description = "Order processing timestamp",
            example = "1650330233.134"
    )
    private Instant processedAt;

    // Только для выходного события
//    private String trackingNumber;
//    private String assignedWarehouse;
//    private Instant estimatedDelivery;
}