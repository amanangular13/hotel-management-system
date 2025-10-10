package com.amanverma.hotelmanagementsystem.user_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicateUserException extends ApiException {
    public DuplicateUserException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}