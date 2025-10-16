package com.amanverma.hotelmanagementsystem.inventory_service.repository;

import com.amanverma.hotelmanagementsystem.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByRoomIdAndDateBetween(Long roomId, LocalDate startDate, LocalDate endDate);
    void deleteByRoomId(Long roomId);
}
