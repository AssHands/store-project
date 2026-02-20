package com.ak.store.catalogue.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldMapNotFoundExceptionTo404() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/api/v1/catalogue/products/42");

        var response = handler.handleNotFound(new NotFoundException("missing"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("missing");
    }

    @Test
    void shouldMapConflictExceptionTo409() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/api/v1/catalogue/categories");

        var response = handler.handleConflict(new ConflictException("conflict"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo(HttpStatus.CONFLICT.getReasonPhrase());
    }
}
