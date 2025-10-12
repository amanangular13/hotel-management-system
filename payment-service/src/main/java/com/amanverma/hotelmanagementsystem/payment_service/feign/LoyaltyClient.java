package com.amanverma.hotelmanagementsystem.payment_service.feign;

import com.amanverma.hotelmanagementsystem.payment_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.payment_service.dto.LoyaltyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "loyalty-service", path = "/loyalty")
public interface LoyaltyClient {

    @GetMapping(path = "/{userId}", produces = "application/json")
    ApiResponse<LoyaltyResponseDTO> getPoints(@PathVariable("userId") Long userId);
}
