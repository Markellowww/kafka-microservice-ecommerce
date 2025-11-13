package com.markellowww.ingestion.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Markelloww
 */

@Service
public class ProcessingService {
    private final WebClient webClient;

    public ProcessingService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity<String> processOrder(String orderJson) {
        String responseBody = webClient.post()
                .uri("/api/process-order")
                .bodyValue(orderJson)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(responseBody);
    }
}