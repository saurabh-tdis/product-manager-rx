package com.example.rx.productmanager.common.exceptions;

public class EntityDuplicateException extends RuntimeException{

    public EntityDuplicateException() {
    }

    public EntityDuplicateException(String message) {
        super(message);
    }

    public EntityDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
