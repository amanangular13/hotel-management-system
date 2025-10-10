package com.amanverma.hotelmanagementsystem.inventory_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "room_inventory",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "date"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Room ID is required")
    @Column(nullable = false)
    private Long roomId;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Availability status is required")
    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (isAvailable == null) isAvailable = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
