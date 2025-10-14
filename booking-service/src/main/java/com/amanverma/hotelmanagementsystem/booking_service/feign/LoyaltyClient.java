package com.amanverma.hotelmanagementsystem.booking_service.feign;

import com.amanverma.hotelmanagementsystem.booking_service.dto.LoyaltyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "loyalty-service", path = "/api/v1/loyalty")
public interface LoyaltyClient {
    @PostMapping("/redeem")
    LoyaltyResponseDTO redeemPoints(@RequestParam Long userId, @RequestParam Double totalAmount);

    @PostMapping("/award")
    void awardPoints(@RequestParam Long userId, @RequestParam Double bookingAmount);
}
