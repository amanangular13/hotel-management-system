package com.amanverma.hotelmanagementsystem.hotel_service.model;

import com.amanverma.hotelmanagementsystem.hotel_service.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hotel name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private List<String> imageUrls;

    private List<String> amenities;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 50)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false, length = 50)
    private String state;

    @NotBlank(message = "Country is required")
    @Column(nullable = false, length = 50)
    private String country;

    @NotBlank(message = "Pin code is required")
    @Column(nullable = false, length = 10)
    private String pinCode;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String contactEmail;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid contact number")
    @Column(nullable = false, length = 15)
    private String contactNumber;

    private Double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    private Long managerId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = Status.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
