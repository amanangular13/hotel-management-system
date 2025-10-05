package com.amanverma.hotelmanagementsystem.api_gateway.advice;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
    private LocalDateTime timestamp;
    private boolean success;
    private HttpStatus httpStatus;
    private String message;
}
