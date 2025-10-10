package com.amanverma.hotelmanagementsystem.inventory_service.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDTO {
    private Long id;
    private Long roomId;
    private LocalDate date;
    private Boolean isAvailable;
}
