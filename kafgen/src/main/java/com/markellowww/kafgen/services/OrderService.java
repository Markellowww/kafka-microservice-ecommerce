package com.markellowww.kafgen.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Markelloww
 */

@Service
public class OrderService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void saveOrder(String order) {
        kafkaTemplate.send("orders.incoming", order);
    }
}
