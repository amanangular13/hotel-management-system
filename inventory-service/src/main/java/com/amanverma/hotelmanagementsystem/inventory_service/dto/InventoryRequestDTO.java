package com.amanverma.hotelmanagementsystem.inventory_service.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestDTO {
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
}
