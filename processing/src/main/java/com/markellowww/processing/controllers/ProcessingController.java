package com.markellowww.processing.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/api")
public class ProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(ProcessingController.class);

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public ProcessingController(ObjectMapper objectMapper,
                                OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @PostMapping("/process-order")
    public ResponseEntity<String> processOrder(@RequestBody String orderJson) throws JsonProcessingException {
        OrderDto orderDto = objectMapper.readValue(orderJson, OrderDto.class);
        logger.debug("Received order for processing: {}", orderDto.getOrderId());
        orderService.createOrder(orderDto);
        return ResponseEntity.ok(objectMapper.writeValueAsString(orderDto));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<String> getOrder(@PathVariable Long orderId) throws JsonProcessingException {
        logger.debug("Fetching order with ID: {}", orderId);
        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(objectMapper.writeValueAsString(orderDto));
    }
}
