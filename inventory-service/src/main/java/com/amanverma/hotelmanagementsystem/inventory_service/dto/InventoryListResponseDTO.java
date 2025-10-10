package com.amanverma.hotelmanagementsystem.inventory_service.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryListResponseDTO {
    private Long roomId;
    private List<InventoryResponseDTO> inventories;
}
