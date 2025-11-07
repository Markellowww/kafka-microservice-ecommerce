package com.markellowww.kafgen;

import com.github.javafaker.Faker;
import com.markellowww.kafgen.enums.OrderStatus;
import com.markellowww.kafgen.enums.ShippingType;
import com.markellowww.kafgen.models.Address;
import com.markellowww.kafgen.models.Order;
import com.markellowww.kafgen.models.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Markelloww
 */

@Component
public class OrderGenerator {
    private final Random random;
    private final Faker faker;

    public OrderGenerator() {
        random = new Random();
        faker = new Faker();
    }

    public Order generateOrder() {
        List<OrderItem> items = randomOrderItems();

        return Order.builder()
                .orderId(generateOrderId())
                .customerId(generateCustomerId())
                .status(randomOrderStatus())
                .shippingType(randomShippingType())
                .items(items)
                .totalAmount(calculateTotalAmount(items))
                .deliveryAddress(generateAddress())
                .timestamp(generateTimestamp())
                .processedAt(null)
                .build();
    }

    private String generateOrderId() {
        return "ORD-" + faker.regexify("[A-Z0-9]{8}");
    }

    private String generateCustomerId() {
        return "CUST-" + faker.regexify("[A-Z0-9]{6}");
    }

    private OrderStatus randomOrderStatus() {
        OrderStatus[] statuses = OrderStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }

    private ShippingType randomShippingType() {
        ShippingType[] shippingTypes = ShippingType.values();
        return shippingTypes[random.nextInt(shippingTypes.length)];
    }

    private List<OrderItem> randomOrderItems() {
        int itemsCount = random.nextInt(5);

        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < itemsCount; i++) {
            orderItems.add(OrderItem.randomOrderItem());
        }

        return orderItems;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Address generateAddress() {
        return Address.builder()
                .city(faker.address().city())
                .street(faker.address().streetAddress())
                .zipCode(faker.address().zipCode())
                .build();
    }

    private Instant generateTimestamp() {
        long currentTime = System.currentTimeMillis();
        long thirtyDaysAgo = currentTime - (30L * 24 * 60 * 60 * 1000);
        long randomTime = thirtyDaysAgo + (long) (random.nextDouble() * (currentTime - thirtyDaysAgo));

        return Instant.ofEpochMilli(randomTime);
    }
}