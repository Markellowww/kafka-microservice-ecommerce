package com.markellowww.ingestion.service;

import com.markellowww.ingestion.models.Order;
import com.markellowww.ingestion.repositories.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Markelloww
 */

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, @Qualifier("objectMapper") ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    public void save(Order order) {
        logger.debug("Order {} deserialized from JSON, sending to MongoDB", order.getOrderId());
        orderRepository.save(order);
        logger.debug("Order {} was successfully sent to MongoDB", order.getOrderId());
    }

    public Order deserializeOrder(ConsumerRecord<String, String> orderJson) {
        logger.debug("Processing the received JSON order");
        Order order = objectMapper.readValue(orderJson.value(), Order.class);
        logger.debug("Order {} was successfully deserialized", order.getOrderId());
        return order;
    }
}
