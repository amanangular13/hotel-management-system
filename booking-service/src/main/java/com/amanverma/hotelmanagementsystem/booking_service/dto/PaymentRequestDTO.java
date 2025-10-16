package com.amanverma.hotelmanagementsystem.booking_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    private String bookingId;
    private Long userId;
    private Double amount;
    private String paymentMethod;
    private Boolean useLoyaltyPoints = false;
}

