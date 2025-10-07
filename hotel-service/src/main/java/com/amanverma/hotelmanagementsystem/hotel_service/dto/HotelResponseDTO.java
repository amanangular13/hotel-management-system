package com.amanverma.hotelmanagementsystem.hotel_service.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String contactEmail;
    private String contactNumber;
    private Double rating;
    private String status;
    private Long managerId;
    private List<String> imageUrls;
    private List<String> amenities;
    private List<RoomResponseDTO> rooms;
}

