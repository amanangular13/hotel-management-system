package com.amanverma.hotelmanagementsystem.payment_service.feign;

import com.amanverma.hotelmanagementsystem.payment_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.payment_service.dto.LoyaltyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "loyalty-service", path = "/loyalty")
public interface LoyaltyClient {

    @GetMapping(path = "/{userId}", produces = "application/json")
    ApiResponse<LoyaltyDTO> getPoints(@PathVariable("userId") Long userId);

    @GetMapping(path = "/add", produces = "application/json")
    ApiResponse<LoyaltyDTO> addPoints(@RequestBody LoyaltyDTO loyaltyDTO);

    @GetMapping(path = "/redeem", produces = "application/json")
    ApiResponse<LoyaltyDTO> redeemPoints(@RequestBody LoyaltyDTO loyaltyDTO);
}
