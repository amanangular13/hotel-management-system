package com.amanverma.hotelmanagementsystem.api_gateway.advice;

import com.amanverma.hotelmanagementsystem.api_gateway.exception.ApiException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        ApiResponse response = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .httpStatus(ex.getStatus())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Something went wrong")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Something went wrong";

        if (ex instanceof ApiException apiEx) {
            status = apiEx.getStatus();
            message = apiEx.getMessage();
        }

        String responseBody = String.format("{\"status\":%d,\"message\":\"%s\"}", status.value(), message);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

