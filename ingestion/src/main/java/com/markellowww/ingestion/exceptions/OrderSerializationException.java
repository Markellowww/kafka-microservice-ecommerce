package com.markellowww.ingestion.exceptions;

public class OrderSerializationException extends RuntimeException {
    public OrderSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
