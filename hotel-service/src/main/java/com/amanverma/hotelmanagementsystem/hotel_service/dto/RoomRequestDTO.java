package com.amanverma.hotelmanagementsystem.hotel_service.dto;

import com.amanverma.hotelmanagementsystem.hotel_service.model.enums.RoomStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDTO {

    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number cannot exceed 20 characters")
    private String roomNumber;

    @NotBlank(message = "Room type is required")
    @Pattern(
            regexp = "SINGLE|DOUBLE|SUITE|DELUXE",
            message = "Room type must be one of: SINGLE, DOUBLE, SUITE, DELUXE"
    )
    private String type;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 10, message = "Capacity cannot exceed 10")
    private Integer capacity;

    @NotBlank(message = "Room status is required")
    private RoomStatus status;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private Double basePrice;

    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    private List<String> imageUrls;
}
