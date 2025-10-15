package com.amanverma.hotelmanagementsystem.booking_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private String transactionId;
    private Double amount;
    private Boolean usedLoyaltyPoints;
    private Integer loyaltyPointsUsed;
    private Double finalAmountCharged;
    private String status;
}
