package com.amanverma.hotelmanagementsystem.booking_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyResponseDTO {
    private Double discountAmount;
    private Integer pointsUsed;
    private Integer pointsAwarded;
}
