package com.amanverma.hotelmanagementsystem.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service", path = "/api/v1/hotels")
public interface HotelClient {
    @GetMapping("/rooms/{roomId}/price")
    Double getRoomPrice(@PathVariable Long roomId);
}
