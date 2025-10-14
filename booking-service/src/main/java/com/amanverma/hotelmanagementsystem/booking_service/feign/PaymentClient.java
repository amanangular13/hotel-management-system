package com.amanverma.hotelmanagementsystem.booking_service.feign;

import com.amanverma.hotelmanagementsystem.booking_service.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", path = "/api/v1/payments")
public interface PaymentClient {
    @PostMapping("/process")
    PaymentResponseDTO processPayment(@RequestParam Long userId, @RequestParam Double amount);
}
