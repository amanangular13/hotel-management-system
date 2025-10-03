package com.amanverma.user_service.controller;

import com.amanverma.user_service.advice.ApiResponse;
import com.amanverma.user_service.dto.UserRequestDTO;
import com.amanverma.user_service.dto.UserResponseDTO;
import com.amanverma.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO userResponseDTO = userService.register(dto);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getByEmail(@PathVariable String email) {
        UserResponseDTO userResponseDTO = userService.getByEmail(email);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getById(id);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateProfile(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO userResponseDTO = userService.updateProfile(id, dto);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/promote")
    public ResponseEntity<ApiResponse<UserResponseDTO>> promote(@RequestParam String email) {
        UserResponseDTO userResponseDTO = userService.promoteUser(email);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDTO>> deactivateUser(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.deactivateUser(id);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }
}