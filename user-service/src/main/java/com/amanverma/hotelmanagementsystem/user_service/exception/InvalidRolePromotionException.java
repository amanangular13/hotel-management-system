package com.amanverma.hotelmanagementsystem.user_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidRolePromotionException extends ApiException {
    public InvalidRolePromotionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
