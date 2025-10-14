package com.amanverma.hotelmanagementsystem.booking_service.controller;

import com.amanverma.hotelmanagementsystem.booking_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingRequestDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingResponseDTO;
import com.amanverma.hotelmanagementsystem.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDTO>> createBooking(@RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, HttpStatus.CREATED));
    }
}
