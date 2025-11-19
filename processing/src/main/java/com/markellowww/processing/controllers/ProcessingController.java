package com.markellowww.processing.controllers;

import com.markellowww.processing.dto.OrderDto;
import com.markellowww.processing.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Markelloww
 */


@RestController
@RequestMapping("/api")
public class ProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(ProcessingController.class);

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    public ProcessingController(@Qualifier("objectMapper") ObjectMapper objectMapper,
                                OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @PostMapping("/process-order")
    public ResponseEntity<String> processOrder(@RequestBody String orderJson) {
        try {
            OrderDto orderDto = objectMapper.readValue(orderJson, OrderDto.class);
            logger.debug("Received order for processing: {}", orderDto.getOrderId());

            orderService.createOrder(orderDto);

            return ResponseEntity.ok(objectMapper.writeValueAsString(orderDto));
        } catch (JacksonException e) {
            logger.error("Invalid JSON format for order: {}", orderJson, e);
            return ResponseEntity.badRequest().body("Invalid JSON format");
        } catch (Exception e) {
            logger.error("Error processing order", e);
            return ResponseEntity.internalServerError().body("Processing failed");
        }
    }
}
