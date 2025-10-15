package com.amanverma.hotelmanagementsystem.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", path = "/inventory")
public interface InventoryClient {

    @GetMapping("/check")
    Boolean isRoomAvailable(@RequestParam Long roomId, @RequestParam String checkIn, @RequestParam String checkOut);
}
