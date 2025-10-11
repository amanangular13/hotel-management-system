package com.amanverma.hotelmanagementsystem.inventory_service.service.impl;

import com.amanverma.hotelmanagementsystem.inventory_service.dto.*;
import com.amanverma.hotelmanagementsystem.inventory_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.inventory_service.model.Inventory;
import com.amanverma.hotelmanagementsystem.inventory_service.repository.InventoryRepository;
import com.amanverma.hotelmanagementsystem.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryListResponseDTO initializeInventory(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<Inventory> inventories = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(startDate.until(endDate).getDays() + 1)
                .map(date -> Inventory.builder()
                        .roomId(roomId)
                        .date(date)
                        .isAvailable(true)
                        .build())
                .collect(Collectors.toList());

        List<Inventory> saved = inventoryRepository.saveAll(inventories);

        return InventoryListResponseDTO.builder()
                .roomId(roomId)
                .inventories(saved.stream()
                        .map(this::toResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public boolean checkAvailability(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).toList();
        List<Inventory> inventoryList = inventoryRepository.findByRoomIdAndDateBetween(roomId, startDate, endDate);

        Set<LocalDate> availableDates = inventoryList.stream()
                .filter(Inventory::getIsAvailable)
                .map(Inventory::getDate)
                .collect(Collectors.toSet());

        return availableDates.containsAll(dates);

    }


    public void markUnavailable(InventoryRequestDTO request) {
        List<Inventory> inventories = inventoryRepository.findByRoomIdAndDateBetween(
                request.getRoomId(), request.getStartDate(), request.getEndDate());
        if (inventories.isEmpty()) {
            throw new ApiException("Inventory not found for given dates", HttpStatus.NOT_FOUND);
        }
        inventories.forEach(inv -> inv.setIsAvailable(false));
        inventoryRepository.saveAll(inventories);
    }

    public void markAvailable(InventoryRequestDTO request) {
        List<Inventory> inventories = inventoryRepository.findByRoomIdAndDateBetween(
                request.getRoomId(), request.getStartDate(), request.getEndDate());
        if (inventories.isEmpty()) {
            throw new ApiException("Inventory not found for given dates", HttpStatus.NOT_FOUND);
        }
        inventories.forEach(inv -> inv.setIsAvailable(true));
        inventoryRepository.saveAll(inventories);
    }

    private InventoryResponseDTO toResponseDTO(Inventory inv) {
        return InventoryResponseDTO.builder()
                .id(inv.getId())
                .roomId(inv.getRoomId())
                .date(inv.getDate())
                .isAvailable(inv.getIsAvailable())
                .build();
    }
}
