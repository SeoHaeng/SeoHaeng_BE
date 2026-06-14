package com.seohaeng.backend.domain.place.batch.exception;

public class TourApiRetryableException extends RuntimeException {
    public TourApiRetryableException(String message) {
        super(message);
    }
}
