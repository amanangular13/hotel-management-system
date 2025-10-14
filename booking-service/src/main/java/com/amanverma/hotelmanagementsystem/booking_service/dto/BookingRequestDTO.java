package com.amanverma.hotelmanagementsystem.booking_service.dto;

import com.amanverma.hotelmanagementsystem.booking_service.model.enums.PaymentMode;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    private Long userId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private PaymentMode paymentMode;
    private Boolean useLoyaltyPoints;
}
