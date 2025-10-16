package com.amanverma.hotelmanagementsystem.booking_service.feign;

import com.amanverma.hotelmanagementsystem.booking_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.booking_service.dto.InventoryRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/check")
    ApiResponse<Boolean> isRoomAvailable(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @PutMapping("/hotel-manager/inventory/mark/booked")
    void markBook(@RequestBody InventoryRequestDTO inventoryRequestDTO);

    @PutMapping("/hotel-manager/inventory/mark/available")
    void markAvailable(InventoryRequestDTO inventoryRequestDTO);
}
