package com.amanverma.hotelmanagementsystem.user_service.controller;

import com.amanverma.hotelmanagementsystem.user_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.user_service.dto.UserResponseDTO;
import com.amanverma.hotelmanagementsystem.user_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/promote")
    public ResponseEntity<ApiResponse<UserResponseDTO>> promote(@RequestParam String email) {
        UserResponseDTO userResponseDTO = adminService.promoteUser(email);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDTO>> deactivateUser(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = adminService.deactivateUser(id);
        return new ResponseEntity<>(ApiResponse.success(userResponseDTO, HttpStatus.OK), HttpStatus.OK);
    }
}
