package com.amanverma.hotelmanagementsystem.hotel_service.service.impl;

import com.amanverma.hotelmanagementsystem.hotel_service.dto.HotelRequestDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.HotelResponseDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.RoomRequestDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.dto.RoomResponseDTO;
import com.amanverma.hotelmanagementsystem.hotel_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.hotel_service.model.Hotel;
import com.amanverma.hotelmanagementsystem.hotel_service.model.Room;
import com.amanverma.hotelmanagementsystem.hotel_service.model.enums.Status;
import com.amanverma.hotelmanagementsystem.hotel_service.model.enums.RoomType;
import com.amanverma.hotelmanagementsystem.hotel_service.repository.HotelRepository;
import com.amanverma.hotelmanagementsystem.hotel_service.repository.RoomRepository;
import com.amanverma.hotelmanagementsystem.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<HotelResponseDTO> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .filter(hotel -> hotel.getStatus().equals(Status.ACTIVE))
                .map(hotel -> modelMapper.map(hotel, HotelResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelResponseDTO getHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        if(!hotel.getStatus().equals(Status.ACTIVE)) {
            throw new ApiException("Hotel is deactivated", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(hotel, HotelResponseDTO.class);
    }

    @Override
    public HotelResponseDTO createHotel(Long userId, HotelRequestDTO hotelRequestDTO) {
        Hotel hotel = Hotel.builder()
                .name(hotelRequestDTO.getName())
                .description(hotelRequestDTO.getDescription())
                .address(hotelRequestDTO.getAddress())
                .city(hotelRequestDTO.getCity())
                .state(hotelRequestDTO.getState())
                .country(hotelRequestDTO.getCountry())
                .pinCode(hotelRequestDTO.getPinCode())
                .contactEmail(hotelRequestDTO.getContactEmail())
                .contactNumber(hotelRequestDTO.getContactNumber())
                .managerId(userId)
                .rating(null)
                .status(Status.ACTIVE)
                .amenities(hotelRequestDTO.getAmenities())
                .imageUrls(hotelRequestDTO.getImageUrls())
                .build();

        Hotel saved = hotelRepository.save(hotel);
        return modelMapper.map(saved, HotelResponseDTO.class);
    }

    @Override
    public HotelResponseDTO updateHotel(Long hotelId, HotelRequestDTO hotelRequestDTO) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        if(!hotel.getStatus().equals(Status.ACTIVE)) {
            throw new ApiException("Cannot update deactivated hotel", HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.hasText(hotelRequestDTO.getName())) hotel.setName(hotelRequestDTO.getName());
        if(StringUtils.hasText(hotelRequestDTO.getDescription())) hotel.setDescription(hotelRequestDTO.getDescription());
        if(StringUtils.hasText(hotelRequestDTO.getContactEmail())) hotel.setContactEmail(hotelRequestDTO.getContactEmail());
        if(StringUtils.hasText(hotelRequestDTO.getContactNumber())) hotel.setContactNumber(hotelRequestDTO.getContactNumber());
        if(hotelRequestDTO.getImageUrls() != null) hotel.setImageUrls(hotelRequestDTO.getImageUrls());
        if(hotelRequestDTO.getAmenities() != null) hotel.setAmenities(hotelRequestDTO.getAmenities());

        Hotel updated = hotelRepository.save(hotel);
        return modelMapper.map(updated, HotelResponseDTO.class);
    }

    @Override
    public void deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        hotel.setStatus(Status.INACTIVE);
        hotelRepository.save(hotel);
    }

    @Override
    public RoomResponseDTO addRoomToHotel(Long hotelId, RoomRequestDTO roomRequestDTO) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        if(!hotel.getStatus().equals(Status.ACTIVE)) {
            throw new ApiException("Cannot add room to deactivated hotel", HttpStatus.BAD_REQUEST);
        }

        Room room = Room.builder()
                .roomNumber(roomRequestDTO.getRoomNumber())
                .type(RoomType.valueOf(roomRequestDTO.getType()))
                .capacity(roomRequestDTO.getCapacity())
                .basePrice(roomRequestDTO.getBasePrice())
                .imageUrls(roomRequestDTO.getImageUrls())
                .status(Status.ACTIVE)
                .hotel(hotel)
                .build();

        Room saved = roomRepository.save(room);
        return modelMapper.map(saved, RoomResponseDTO.class);
    }

    @Override
    public RoomResponseDTO updateRoom(Long roomId, RoomRequestDTO roomRequestDTO) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Room not found", HttpStatus.NOT_FOUND));

        if(StringUtils.hasText(roomRequestDTO.getRoomNumber())) room.setRoomNumber(roomRequestDTO.getRoomNumber());
        if(StringUtils.hasText(roomRequestDTO.getType())) room.setType(RoomType.valueOf(roomRequestDTO.getType()));
        if(StringUtils.hasText(roomRequestDTO.getStatus().toString())) room.setStatus(Status.valueOf(roomRequestDTO.getStatus().toString()));
        if(roomRequestDTO.getCapacity() != null) room.setCapacity(roomRequestDTO.getCapacity());
        if(roomRequestDTO.getBasePrice() != null) room.setBasePrice(roomRequestDTO.getBasePrice());
        if(roomRequestDTO.getImageUrls() != null) room.setImageUrls(roomRequestDTO.getImageUrls());

        Room updated = roomRepository.save(room);
        return modelMapper.map(updated, RoomResponseDTO.class);
    }

    @Override
    public Double getRoomPrice(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Room not found", HttpStatus.NOT_FOUND));
        return room.getBasePrice();
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Room not found", HttpStatus.NOT_FOUND));
        room.setStatus(Status.INACTIVE);
        roomRepository.save(room);
    }
}