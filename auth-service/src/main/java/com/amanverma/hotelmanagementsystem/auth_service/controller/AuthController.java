package com.amanverma.hotelmanagementsystem.auth_service.controller;

import com.amanverma.hotelmanagementsystem.auth_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.auth_service.dto.*;
import com.amanverma.hotelmanagementsystem.auth_service.dto.LoginRequestDTO;
import com.amanverma.hotelmanagementsystem.auth_service.dto.LoginResponseDTO;
import com.amanverma.hotelmanagementsystem.auth_service.dto.RegisterRequestDTO;
import com.amanverma.hotelmanagementsystem.auth_service.dto.RegisterResponseDTO;
import com.amanverma.hotelmanagementsystem.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO loginResponse = authService.login(request);

        ApiResponse<LoginResponseDTO> response = ApiResponse.<LoginResponseDTO>builder()
                .data(loginResponse)
                .error(null)
                .success(true)
                .message("Successfully Authenticated")
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", loginResponse.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO registerResponse = authService.register(request);

        ApiResponse<RegisterResponseDTO> response = ApiResponse.<RegisterResponseDTO>builder()
                .data(registerResponse)
                .error(null)
                .success(true)
                .message("Successfully Registered")
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
