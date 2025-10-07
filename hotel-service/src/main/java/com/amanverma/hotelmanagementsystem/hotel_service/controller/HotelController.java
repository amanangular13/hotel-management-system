package com.amanverma.hotelmanagementsystem.hotel_service.controller;

import com.amanverma.hotelmanagementsystem.hotel_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.HotelResponseDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelResponseDTO>>> getAllHotels() {
        List<HotelResponseDTO> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(ApiResponse.<List<HotelResponseDTO>>builder()
                .success(true)
                .message("Hotels fetched successfully")
                .data(hotels)
                .build());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelResponseDTO>> getHotelById(@PathVariable Long hotelId) {
        HotelResponseDTO hotel = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(ApiResponse.<HotelResponseDTO>builder()
                .success(true)
                .message("Hotel details fetched successfully")
                .data(hotel)
                .build());
    }
}