package com.markellowww.kafgen.controllers;

import com.markellowww.kafgen.OrderGenerator;
import com.markellowww.kafgen.models.Order;
import com.markellowww.kafgen.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Markelloww
 */

@RestController
@Tag(name = "Order Generator", description = "API for order generation")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final OrderGenerator generator;

    public OrderController(OrderService orderService, OrderGenerator generator) {
        this.orderService = orderService;
        this.generator = generator;
        logger.debug("OrderController and OrderService were initialized");
    }

    @Operation(
            summary = "Create one order",
            description = "Generates and stores one random order in Kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Order.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping(
            value = "/order",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Order> createOrder() {
        logger.info("Received request to create new order");

        Order order = generator.generateOrder();
        orderService.saveOrder(order);

        logger.info("Order created and sent successfully with ID: {}", order.getOrderId());
        return ResponseEntity.ok(order);
    }

    @Operation(
            summary = "Create multiple orders",
            description = "Generates and saves the specified number of orders"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Order[].class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid count parameter"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping(
            value = "/order/batch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Order>> createOrders(@RequestParam(defaultValue = "1") int count) {
        logger.info("Received request to create {} orders", count);

        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }

        int maxBatchSize = 100;
        if (count > maxBatchSize) {
            throw new IllegalArgumentException("Maximum batch size is " + maxBatchSize);
        }

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Order order = generator.generateOrder();
            orderService.saveOrder(order);
            orders.add(order);
        }

        logger.info("Successfully created {} orders", orders.size());
        return ResponseEntity.ok(orders);
    }
}
