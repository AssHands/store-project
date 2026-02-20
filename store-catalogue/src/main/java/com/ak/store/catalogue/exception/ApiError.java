package com.ak.store.catalogue.exception;

import java.time.Instant;

public record ApiError(
        int status,
        String error,
        String message,
        Instant timestamp,
        String path
) {
}
