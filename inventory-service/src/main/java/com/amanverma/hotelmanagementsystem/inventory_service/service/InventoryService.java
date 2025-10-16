package com.amanverma.hotelmanagementsystem.inventory_service.service;

import com.amanverma.hotelmanagementsystem.inventory_service.dto.*;

import java.time.LocalDate;

public interface InventoryService {

    InventoryListResponseDTO initializeInventory(Long roomId, LocalDate startDate, LocalDate endDate);

    boolean checkAvailability(Long roomId, LocalDate startDate, LocalDate endDate);

    void markUnavailable(InventoryRequestDTO request);

    void markAvailable(InventoryRequestDTO request);

    void deleteInavtice(Long roomId);
}
