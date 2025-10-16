package com.amanverma.hotelmanagementsystem.booking_service.feign;

import com.amanverma.hotelmanagementsystem.booking_service.advice.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service", path = "/hotels")
public interface HotelClient {

    @GetMapping("/rooms/{roomId}/price")
    ApiResponse<Double> getRoomPrice(@PathVariable Long roomId);
}
