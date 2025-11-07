package com.markellowww.kafgen.services;

import com.markellowww.kafgen.models.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Markelloww
 */

@Service
public class OrderService {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderService(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void saveOrder(Order order) {
        kafkaTemplate.send("orders.incoming", order);
    }
}
