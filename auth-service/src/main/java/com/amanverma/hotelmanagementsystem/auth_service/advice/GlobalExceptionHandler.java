package com.amanverma.hotelmanagementsystem.auth_service.advice;

import com.amanverma.hotelmanagementsystem.auth_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.auth_service.util.JsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final JsonService jsonService;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {

        String msg = jsonService.extractMessage(ex.getMessage());
        String finalMsg = (Objects.equals(msg, "Unknown error")) ? ex.getMessage() : msg;
        HttpStatus status = (Objects.equals(msg, "Unknown error")) ? HttpStatus.UNAUTHORIZED : HttpStatus.CONFLICT;

        ApiResponse<Object> response = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .data(null)
                .error(ex.getMessage())
                .message(finalMsg)
                .httpStatus(status)
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse(ex.getMessage());

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .data(null)
                .error(errorMessage)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .data(null)
                .error("Internal Server Error: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

