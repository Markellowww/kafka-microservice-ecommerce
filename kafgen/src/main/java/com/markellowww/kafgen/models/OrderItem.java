package com.markellowww.kafgen.models;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author Markelloww
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;

    private static final Faker faker = new Faker();
    private static final Random RANDOM = new Random();

    public static OrderItem randomOrderItem() {
        return OrderItem.builder()
                .productId(faker.regexify("[A-Z]{3}-[0-9]{4}"))
                .productName(faker.commerce().productName())
                .quantity(1 + RANDOM.nextInt(10))
                .price(new BigDecimal(faker.commerce().price(10.0, 1000.0)))
                .build();
    }
}