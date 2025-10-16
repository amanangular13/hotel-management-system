package com.amanverma.hotelmanagementsystem.payment_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyDTO {

    private Long userId;
    private Integer points;
}

