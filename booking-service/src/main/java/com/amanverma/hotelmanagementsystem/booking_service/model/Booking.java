package com.amanverma.hotelmanagementsystem.booking_service.model;

import com.amanverma.hotelmanagementsystem.booking_service.model.enums.BookingStatus;
import com.amanverma.hotelmanagementsystem.booking_service.model.enums.PaymentMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long roomId;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    private Double totalAmount;
    private Boolean usedLoyaltyPoints;
    private Double loyaltyDiscount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (status == null) this.status = BookingStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
