package com.amanverma.auth_service.controller;

import com.amanverma.auth_service.advice.ApiResponse;
import com.amanverma.auth_service.dto.AuthRequestDTO;
import com.amanverma.auth_service.dto.AuthResponseDTO;
import com.amanverma.auth_service.feign.UserClient;
import com.amanverma.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO authResponse = authService.login(request);

        ApiResponse<AuthResponseDTO> response = ApiResponse.<AuthResponseDTO>builder()
                .data(authResponse)
                .error(null)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }
}