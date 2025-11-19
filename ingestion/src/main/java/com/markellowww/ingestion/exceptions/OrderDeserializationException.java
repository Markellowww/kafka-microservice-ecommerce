package com.markellowww.ingestion.exceptions;

public class OrderDeserializationException extends RuntimeException {
    public OrderDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
