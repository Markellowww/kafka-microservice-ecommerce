package com.markellowww.ingestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markellowww.ingestion.enums.ShippingType;
import com.markellowww.ingestion.exceptions.OrderDeserializationException;
import com.markellowww.ingestion.exceptions.OrderMongoDbSavingException;
import com.markellowww.ingestion.exceptions.OrderSerializationException;
import com.markellowww.ingestion.models.Order;
import com.markellowww.ingestion.repositories.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Markelloww
 */

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProcessingService processingService;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        ProcessingService processingService,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.processingService = processingService;
        this.objectMapper = objectMapper;
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
            throw new OrderMongoDbSavingException("Failed to save order to MongoDB: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<String> sendOrderToProcessing(Order order) {
        logger.debug("Order {} is sending to /api/process-order", order.getOrderId());
        String orderJson = serializeOrder(order);
        ResponseEntity<String> response = processingService.processOrder(orderJson);
        logger.debug("Order {} processed successfully", order.getOrderId());
        return response;
    }

    public String serializeOrder(Order order) {
        try {
            logger.debug("Starting to serialize the received JSON order");
            String orderJson = objectMapper.writeValueAsString(order);
            logger.debug("Order {} was successfully serialized", order.getOrderId());
            return orderJson;
        } catch (Exception e) {
            throw new OrderSerializationException("Order deserialization failed", e);
        }
    }

    public Order deserializeOrder(ConsumerRecord<String, String> orderJson) {
        try {
            logger.debug("Starting to deserialize the received JSON order");
            Order order = objectMapper.readValue(orderJson.value(), Order.class);
            validateOrder(order);
            logger.debug("Order {} was successfully deserialized", order.getOrderId());
            return order;
        } catch (Exception e) {
            throw new OrderDeserializationException("Order deserialization failed", e);
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
