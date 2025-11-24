package com.markellowww.processing.controllers;

import com.markellowww.processing.dto.OrderDto;
import com.markellowww.processing.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Markelloww
 */


@RestController
public class ProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(ProcessingController.class);

    private final OrderService orderService;

    public ProcessingController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/process/orders")
    public ResponseEntity<String> processOrder(@RequestBody String orderJson) {
        logger.debug("Received order for processing: {}", orderJson);
        OrderDto orderDto = orderService.deserializeOrderDto(orderJson);
        orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderJson);
    }

    @GetMapping("process/orders/{orderId}")
    public ResponseEntity<String> getOrder(@PathVariable Long orderId) {
        logger.debug("Fetching order with ID: {}", orderId);
        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderService.serializeOrderDto(orderDto));
    }
}
