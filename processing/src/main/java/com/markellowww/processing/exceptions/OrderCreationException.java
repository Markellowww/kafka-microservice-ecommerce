package com.markellowww.processing.exceptions;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
