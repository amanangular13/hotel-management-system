package com.amanverma.hotelmanagementsystem.booking_service.feign;

import com.amanverma.hotelmanagementsystem.booking_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.booking_service.dto.PaymentRequestDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", path = "/payments")
public interface PaymentClient {

    @PutMapping("/refund")
    void refund(@RequestParam String transactionId);

    @PostMapping("/process")
    ApiResponse<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO paymentRequestDTO);
}
