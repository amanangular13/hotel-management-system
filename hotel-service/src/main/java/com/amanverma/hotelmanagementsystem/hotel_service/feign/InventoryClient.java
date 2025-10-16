package com.amanverma.hotelmanagementsystem.hotel_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "inventory-service", path = "/hotel-manager/inventory")
public interface InventoryClient {

    @PutMapping(path = "/delete/inactive/{roomId}", produces = "application/json")
    void deleteInactive(@PathVariable("roomId") Long roomId);
}
