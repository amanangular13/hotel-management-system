package com.amanverma.hotelmanagementsystem.hotel_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequestDTO {

    @NotBlank(message = "Hotel name is required")
    @Size(max = 100, message = "Hotel name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Hotel description is required")
    private String description;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;

    @NotBlank(message = "Pin code is required")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "Pin code must be between 5 and 10 digits")
    private String pinCode;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String contactNumber;

    private List<String> imageUrls;

    private List<String> amenities;
}
