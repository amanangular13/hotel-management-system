package com.amanverma.hotelmanagementsystem.hotel_service.controller;

import com.amanverma.hotelmanagementsystem.hotel_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/hotels")
@RequiredArgsConstructor
public class AdminController {

    private final HotelService hotelService;

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Hotel deactivated successfully")
                .build());
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long roomId) {
        hotelService.deleteRoom(roomId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Room deactivated successfully")
                .build());
    }
}
