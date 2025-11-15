package com.markellowww.ingestion.service;

import com.markellowww.ingestion.enums.ShippingType;
import com.markellowww.ingestion.models.Order;
import com.markellowww.ingestion.repositories.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Markelloww
 */

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ProcessingService processingService;

    public OrderService(OrderRepository orderRepository,
                        ObjectMapper objectMapper,
                        ProcessingService processingService) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.processingService = processingService;
    }

    public void saveToMongo(Order order) {
        logger.debug("Order {} deserialized from JSON", order.getOrderId());
        if (orderRepository.existsById(order.getOrderId())) {
            logger.warn("Order {} already saved, skipping", order.getOrderId());
            return;
        }
        try {
            logger.debug("Sending order {} to MongoDB", order.getOrderId());
            orderRepository.save(order);
            logger.debug("Order {} was successfully sent to MongoDB", order.getOrderId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save order to MongoDB: " + e.getMessage(), e);
        }
    }

    public CompletableFuture<ResponseEntity<String>> sendOrderToProcessing(Order order) {
        logger.debug("Order {} is sending to /api/process-order", order.getOrderId());

        try {
            String orderJson = objectMapper.writeValueAsString(order);
            return processingService.processOrderAsync(orderJson)
                    .exceptionally(throwable -> {
                        logger.error("Async processing failed for order: {}", order.getOrderId(), throwable);
                        throw throwable instanceof RuntimeException ?
                                (RuntimeException) throwable :
                                new RuntimeException("Async processing failed", throwable);
                    });
        } catch (JacksonException e) {
            logger.error("Failed to serialize order: {}", order.getOrderId(), e);
            return CompletableFuture.failedFuture(
                    new RuntimeException("Failed to serialize order", e));
        }
    }

    public Order deserializeOrder(ConsumerRecord<String, String> orderJson) {
        try {
            logger.debug("Processing the received JSON order");
            Order order = objectMapper.readValue(orderJson.value(), Order.class);
            validateOrder(order);
            logger.debug("Order {} was successfully deserialized", order.getOrderId());
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Order deserialization failed", e);
        }
    }

    public void determineShippingType(Order order) {
        if (order.getShippingType() != null) {
            logger.warn("Order {} already has ShippingType, skipping", order.getOrderId());
            return;
        }

        logger.debug("ShippingType of order {} is being determined", order.getOrderId());
        order.setShippingType(randomShippingType());
        order.setProcessedAt(Instant.now());
        logger.debug("ShippingType of order {} has been determined", order.getOrderId());
    }

    private ShippingType randomShippingType() {
        ShippingType[] shippingTypes = ShippingType.values();
        return shippingTypes[ThreadLocalRandom.current().nextInt(shippingTypes.length)];
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        } else if (order.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
    }
}
