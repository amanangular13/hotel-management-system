package com.amanverma.hotelmanagementsystem.loyalty_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyResponseDTO {

    private Long userId;
    private Integer points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

