package com.amanverma.hotelmanagementsystem.hotel_service.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {

    private Long id;
    private String roomNumber;
    private String type;
    private Integer capacity;
    private Double price;
    private String status;
    private List<String> imageUrls;
}
