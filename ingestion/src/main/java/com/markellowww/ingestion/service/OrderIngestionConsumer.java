//package com.markellowww.ingestion.service;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Service;
//
///**
// * @author Markelloww
// */
//
//@Service
//public class OrderIngestionConsumer {
//    private static final Logger log = LoggerFactory.getLogger(OrderIngestionConsumer.class);
//
//    @KafkaListener(topics = "orders.incoming", groupId = "order-ingestion-service")
//    public void consumeOrder(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        try {
//            log.info("Received order: Key={}, Partition={}, Offset={}",
//                    record.key(), record.partition(), record.offset());
//
//            // Обработка входящего заказа
//            processOrder(record.value());
//
//            ack.acknowledge();
//            log.info("Order processed successfully: {}", record.key());
//
//        } catch (Exception e) {
//            log.error("Error processing order: {}", record.key(), e);
//            // Здесь можно добавить логику для dead letter queue
//        }
//    }
//
//    private void processOrder(String orderData) {
//        // Логика обработки заказа
//        // Сохранение в MongoDB, валидация, etc.
//    }
//}
