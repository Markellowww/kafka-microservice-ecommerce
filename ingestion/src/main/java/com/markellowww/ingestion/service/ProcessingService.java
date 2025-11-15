package com.markellowww.ingestion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

/**
 * @author Markelloww
 */

@Service
public class ProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessingService.class);

    private final WebClient webClient;

    public ProcessingService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> processOrderAsync(String orderJson) {
        return webClient.post()
                .uri("/api/process-order")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(orderJson)
                .retrieve()
                .toEntity(String.class)
                .toFuture()
                .exceptionally(throwable -> {
                    logger.error("HTTP request failed for order processing", throwable);
                    throw new RuntimeException("HTTP request failed", throwable);
                });
    }
}