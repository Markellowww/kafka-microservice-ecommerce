package com.markellowww.kafgen.models;

import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(
            description = "Unique product identifier",
            example = "ORH-7452",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String productId;

    @Schema(
            description = "Product name",
            example = "Smartphone Samsung Galaxy S24 Ultra 512GB",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String productName;

    @Schema(
            description = "Quantity of goods in the order",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer quantity;

    @Schema(
            description = "Unit price",
            example = "1199.99",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
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