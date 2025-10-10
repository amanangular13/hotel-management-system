package com.amanverma.hotelmanagementsystem.inventory_service.controller;

import com.amanverma.hotelmanagementsystem.inventory_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.inventory_service.dto.*;
import com.amanverma.hotelmanagementsystem.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/hotel-manager/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse<InventoryListResponseDTO>> initializeInventory(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        InventoryListResponseDTO response = inventoryService.initializeInventory(roomId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(response, HttpStatus.CREATED));
    }

    @PutMapping("/mark/booked")
    public ResponseEntity<ApiResponse<Void>> markUnavailable(@RequestBody InventoryRequestDTO request) {
        inventoryService.markUnavailable(request);
        return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK));
    }

    @PutMapping("/mark/available")
    public ResponseEntity<ApiResponse<Void>> markAvailable(@RequestBody InventoryRequestDTO request) {
        inventoryService.markAvailable(request);
        return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK));
    }
}
