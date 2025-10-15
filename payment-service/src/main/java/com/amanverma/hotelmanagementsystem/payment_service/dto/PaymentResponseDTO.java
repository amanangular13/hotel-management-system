package com.amanverma.hotelmanagementsystem.payment_service.dto;

import com.amanverma.hotelmanagementsystem.payment_service.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long bookingId;
    private Double amount;
    private PaymentStatus status;
    private String transactionId;
    private String paymentMethod;
    private Boolean useLoyaltyPoints;
    private Integer loyaltyPointsUsed;
    private Double finalAmountCharged;
    private LocalDateTime createdAt;
}
