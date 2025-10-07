package com.amanverma.hotelmanagementsystem.hotel_service.controller;

import com.amanverma.hotelmanagementsystem.hotel_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.*;
import com.amanverma.hotelmanagementsystem.hotel_service.feign.UserClient;
import com.amanverma.hotelmanagementsystem.hotel_service.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hotel-manager/hotels")
@RequiredArgsConstructor
public class HotelManagerController {

    private final HotelService hotelService;
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<ApiResponse<HotelResponseDTO>> createHotel(@Valid @RequestBody HotelRequestDTO hotelRequestDTO,
                                                                     @RequestHeader("X-User-Id") String email) {

        UserResponseDTO user = userClient.getUserByEmail(email).getData();
        HotelResponseDTO hotel = hotelService.createHotel(user.getId(), hotelRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<HotelResponseDTO>builder()
                        .success(true)
                        .message("Hotel created successfully")
                        .data(hotel)
                        .build());
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelResponseDTO>> updateHotel(@PathVariable Long hotelId,
                                                                     @Valid @RequestBody HotelRequestDTO hotelRequestDTO) {
        HotelResponseDTO hotel = hotelService.updateHotel(hotelId, hotelRequestDTO);
        return ResponseEntity.ok(ApiResponse.<HotelResponseDTO>builder()
                .success(true)
                .message("Hotel updated successfully")
                .data(hotel)
                .build());
    }

    @PostMapping("/{hotelId}/rooms")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> addRoomToHotel(@PathVariable Long hotelId,
                                                                       @Valid @RequestBody RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO room = hotelService.addRoomToHotel(hotelId, roomRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoomResponseDTO>builder()
                        .success(true)
                        .message("Room added successfully")
                        .data(room)
                        .build());
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> updateRoom(@PathVariable Long roomId,
                                                                   @Valid @RequestBody RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO room = hotelService.updateRoom(roomId, roomRequestDTO);
        return ResponseEntity.ok(ApiResponse.<RoomResponseDTO>builder()
                .success(true)
                .message("Room updated successfully")
                .data(room)
                .build());
    }
}
