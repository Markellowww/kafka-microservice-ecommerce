package com.markellowww.ingestion.exceptions;

public class OrderMongoDbSavingException extends RuntimeException {
    public OrderMongoDbSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}
