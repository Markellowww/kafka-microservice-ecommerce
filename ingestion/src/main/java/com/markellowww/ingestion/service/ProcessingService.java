package com.markellowww.ingestion.service;

import com.markellowww.ingestion.exceptions.OrderProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Markelloww
 */

@Service
@RequiredArgsConstructor
public class ProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessingService.class);

    private final WebClient webClient;

    public ResponseEntity<String> processOrder(String orderJson) {
        logger.info("Starting order processing request");

        try {
            logger.debug("Sending order data: {}", orderJson);

            return webClient.post()
                    .uri("/process/orders")
                    .bodyValue(orderJson)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (Exception e) {
            throw new OrderProcessingException("HTTP request failed", e);
        }
    }
}