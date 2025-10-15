package com.amanverma.hotelmanagementsystem.hotel_service.service;

import com.amanverma.hotelmanagementsystem.hotel_service.dto.HotelRequestDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.HotelResponseDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.RoomRequestDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.RoomResponseDTO;

import java.util.List;

public interface HotelService {

    void deleteHotel(Long hotelId);

    void deleteRoom(Long roomId);

    List<HotelResponseDTO> getAllHotels();

    HotelResponseDTO getHotelById(Long hotelId);

    HotelResponseDTO createHotel(Long userId, HotelRequestDTO hotelRequestDTO);

    HotelResponseDTO updateHotel(Long hotelId, HotelRequestDTO hotelRequestDTO);

    RoomResponseDTO addRoomToHotel(Long hotelId, RoomRequestDTO roomRequestDTO);

    RoomResponseDTO updateRoom(Long roomId, RoomRequestDTO roomRequestDTO);

    Double getRoomPrice(Long roomId);
}
