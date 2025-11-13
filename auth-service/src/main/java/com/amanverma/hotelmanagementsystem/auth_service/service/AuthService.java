package com.amanverma.hotelmanagementsystem.auth_service.service;

import com.amanverma.hotelmanagementsystem.auth_service.dto.*;
import com.amanverma.hotelmanagementsystem.auth_service.dto.*;
import com.amanverma.hotelmanagementsystem.auth_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.auth_service.feign.UserClient;
import com.amanverma.hotelmanagementsystem.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        UserResponseDTO user;
        try {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            user = userClient.register(request).getData();
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return modelMapper.map(user, RegisterResponseDTO.class);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        UserResponseDTO user;
        try {
            user = userClient.getUserByEmail(request.getEmail()).getData();
        } catch (Exception e) {
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!user.isActive()) {
            throw new ApiException("User account is deactivated", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                List.of(user.getRole().name())
        );
        return new LoginResponseDTO(token, user.getRole().name());
    }
}
