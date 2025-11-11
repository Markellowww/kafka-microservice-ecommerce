package com.markellowww.kafgen.exceptions;

import com.markellowww.kafgen.dto.ErrorResponseDto;
import org.apache.commons.lang3.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * @author Markelloww
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<ErrorResponseDto> handleSerializationException(SerializationException e) {
        logger.warn("Order serialization error: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Invalid Order Data")
                        .message("Failed to process data - invalid format")
                        .details("Please check the data and try again")
                        .build()
        );
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<ErrorResponseDto> handleKafkaSendException(KafkaException e) {
        logger.error("Kafka send error: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                ErrorResponseDto.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .error("Service Temporarily Unavailable")
                        .message("Unable to process data at this time")
                        .details("Please try again later")
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        logger.warn("Exception error: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponseDto.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error("Internal Server Error")
                        .message("An unexpected error occurred")
                        .details("Please contact support if the problem persists")
                        .build()
        );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentExceptionException(IllegalArgumentException e) {
        logger.warn("Illegal argument error: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Invalid request parameters")
                        .message("Failed to process data - Invalid request parameters")
                        .details("Please check the data and try again")
                        .build()
        );
    }
}