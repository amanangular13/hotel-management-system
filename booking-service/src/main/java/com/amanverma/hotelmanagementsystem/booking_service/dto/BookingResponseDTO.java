package com.amanverma.hotelmanagementsystem.booking_service.dto;

import com.amanverma.hotelmanagementsystem.booking_service.model.enums.BookingStatus;
import com.amanverma.hotelmanagementsystem.booking_service.model.enums.PaymentMode;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long bookingId;
    private Long userId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private PaymentMode paymentMode;
    private Double totalAmount;
    private Double loyaltyDiscount;
    private Boolean usedLoyaltyPoints;
}
