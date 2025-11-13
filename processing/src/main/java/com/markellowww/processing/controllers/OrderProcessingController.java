package com.markellowww.processing.controllers;

import com.markellowww.processing.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Markelloww
 */


@RestController
@RequestMapping("/api")
public class OrderProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingController.class);

    private final ObjectMapper objectMapper;

    public OrderProcessingController(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/process-order")
    public ResponseEntity<String> processOrder(@RequestBody String orderJson) {
        Order order = objectMapper.readValue(orderJson, Order.class);
        logger.debug("Received order for processing: {}", order.getOrderId());


        System.out.println("ПОЛУЧЕННЫЙ ЗАКАЗ: " + order);
        // 1. Если заказ срочный, то кеширует в Redis

        // 2. Проверяет наличие товара, валидирует данные

        // 3. Вызывает fulfillment сервис

        return ResponseEntity.ok(objectMapper.writeValueAsString(order));
    }
}
