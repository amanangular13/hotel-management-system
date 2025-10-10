package com.amanverma.hotelmanagementsystem.inventory_service.advice;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private Boolean success;
    private HttpStatus httpStatus;
    private String message;
    private T data;
    private Object error;

    public static <T> ApiResponse<T> success(T data, HttpStatus httpStatus) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .httpStatus(httpStatus)
                .message("Successful")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(Exception ex, HttpStatus httpStatus, Object errors) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .httpStatus(httpStatus)
                .message(ex.getMessage())
                .error(errors)
                .build();
    }
}
