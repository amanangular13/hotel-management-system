package com.amanverma.hotelmanagementsystem.loyalty_service.controller;

import com.amanverma.hotelmanagementsystem.loyalty_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.loyalty_service.dto.LoyaltyRequestDTO;
import com.amanverma.hotelmanagementsystem.loyalty_service.dto.LoyaltyResponseDTO;
import com.amanverma.hotelmanagementsystem.loyalty_service.service.LoyaltyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<LoyaltyResponseDTO>> addPoints(@Valid @RequestBody LoyaltyRequestDTO requestDTO) {
        LoyaltyResponseDTO responseDTO = loyaltyService.addPoints(requestDTO);
        ApiResponse<LoyaltyResponseDTO> response = ApiResponse.<LoyaltyResponseDTO>builder()
                .success(true)
                .httpStatus(HttpStatus.OK)
                .message("Points added successfully")
                .data(responseDTO)
                .error(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/redeem")
    public ResponseEntity<ApiResponse<LoyaltyResponseDTO>> redeemPoints(@Valid @RequestBody LoyaltyRequestDTO requestDTO) {
        LoyaltyResponseDTO responseDTO = loyaltyService.redeemPoints(requestDTO);
        ApiResponse<LoyaltyResponseDTO> response = ApiResponse.<LoyaltyResponseDTO>builder()
                .success(true)
                .httpStatus(HttpStatus.OK)
                .message("Points redeemed successfully")
                .data(responseDTO)
                .error(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<LoyaltyResponseDTO>> getPoints(@PathVariable Long userId) {
        LoyaltyResponseDTO responseDTO = loyaltyService.getPointsByUserId(userId);
        ApiResponse<LoyaltyResponseDTO> response = ApiResponse.<LoyaltyResponseDTO>builder()
                .success(true)
                .httpStatus(HttpStatus.OK)
                .message("Points fetched successfully")
                .data(responseDTO)
                .error(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

