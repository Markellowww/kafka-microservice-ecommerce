package com.markellowww.ingestion.service;

import com.markellowww.ingestion.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * @author Markelloww
 */

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final OrderService orderService;

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "orders.incoming")
    public void consumeOrder(ConsumerRecord<String, String> orderJson, Acknowledgment ack) {
        logger.debug("Received message from Kafka topic: orders.incoming");

        Order order = orderService.deserializeOrder(orderJson);
        orderService.determineShippingType(order);
        orderService.saveToMongo(order);
        orderService.sendOrderToProcessing(order);

        ack.acknowledge();
        logger.debug("Message acknowledged successfully for order {}", order.getOrderId());
    }
}