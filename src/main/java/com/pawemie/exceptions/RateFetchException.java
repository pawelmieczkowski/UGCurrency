package com.pawemie.exceptions;

public class RateFetchException extends Exception {
    public RateFetchException(String message) {
        super(message);
    }

    public RateFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
