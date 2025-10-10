package com.amanverma.hotelmanagementsystem.inventory_service.controller;


import com.amanverma.hotelmanagementsystem.inventory_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class PublicController {
    private final InventoryService inventoryService;

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        boolean available = inventoryService.checkAvailability(roomId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(available, HttpStatus.OK));
    }
}
