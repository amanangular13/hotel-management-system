package com.amanverma.hotelmanagementsystem.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "loyalty-service", path = "/loyalty")
public interface LoyaltyClient {

    @PostMapping("/add")
    void awardPoints(@RequestParam Long userId, @RequestParam Double bookingAmount);
}