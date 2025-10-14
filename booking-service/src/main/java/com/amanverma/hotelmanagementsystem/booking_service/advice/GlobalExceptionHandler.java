package com.amanverma.hotelmanagementsystem.booking_service.advice;

import com.amanverma.hotelmanagementsystem.booking_service.exception.ApiException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex, ex.getStatus(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex, HttpStatus.INTERNAL_SERVER_ERROR, null));
    }
}
