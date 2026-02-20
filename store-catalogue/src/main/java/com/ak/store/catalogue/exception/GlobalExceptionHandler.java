package com.ak.store.catalogue.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleBadRequest(Exception exception, HttpServletRequest request) {
        var message = exception instanceof MethodArgumentNotValidException notValid
                ? notValid.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "))
                : exception.getMessage();

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandled(Exception exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        var body = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                Instant.now(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(body);
    }

    private String formatFieldError(FieldError fieldError) {
        return "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
