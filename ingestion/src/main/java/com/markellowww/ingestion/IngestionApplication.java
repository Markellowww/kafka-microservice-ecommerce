package com.markellowww.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.mongodb.autoconfigure.DataMongoAutoConfiguration;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author Markelloww
 */

@EnableKafka
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataMongoAutoConfiguration.class})
public class IngestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(IngestionApplication.class, args);
    }
}
