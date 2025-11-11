package com.markellowww.kafgen.configs;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Markelloww
 */

@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.acks}")
    private String acks;

    @Value("${kafka.producer.retries}")
    private String retries;

    @Value("${kafka.producer.enable-idempotence}")
    private String idempotence;

    @Value("${kafka.producer.batch-size}")
    private String batchSize;

    @Value("${kafka.producer.max-in-flight-requests-per-connection}")
    private String maxInFlight;

    @Value("${kafka.producer.linger-ms}")
    private String lingerMs;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlight);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
