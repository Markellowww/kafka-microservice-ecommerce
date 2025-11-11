package com.markellowww.kafgen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markellowww.kafgen.models.Order;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Markelloww
 */

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderService(KafkaTemplate<String, String> kafkaTemplate,
                        ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void saveOrder(Order order) {
        logger.debug("Processing order with ID: {}", order.getOrderId());

        try {
            String orderJson = objectMapper.writeValueAsString(order);
            logger.debug("Order serialized to JSON, sending to Kafka topic: orders.incoming");

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    "orders.incoming",
                    null,
                    "order",
                    orderJson
            );

            var future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send order to Kafka: {}", order.getOrderId(), ex);
                    throw new KafkaException("Failed to send order to Kafka: " + order.getOrderId(), ex);
                } else {
                    logger.debug("Order sent to Kafka successfully: {}", order.getOrderId());
                }
            });

        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to serialize order to JSON: " + order.getOrderId(), e);
        }
    }
}
