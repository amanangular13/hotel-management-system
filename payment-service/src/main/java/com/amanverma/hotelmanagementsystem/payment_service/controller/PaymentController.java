package com.amanverma.hotelmanagementsystem.payment_service.controller;

import com.amanverma.hotelmanagementsystem.payment_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.payment_service.dto.PaymentRequestDTO;
import com.amanverma.hotelmanagementsystem.payment_service.dto.PaymentResponseDTO;
import com.amanverma.hotelmanagementsystem.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> processPayment(
            @Valid @RequestBody PaymentRequestDTO request) {

        PaymentResponseDTO response = paymentService.processPayment(request);
        return ResponseEntity.ok(ApiResponse.success(response, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                paymentService.getPaymentById(id), HttpStatus.OK));
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> getPaymentByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(ApiResponse.success(
                paymentService.getPaymentByTransactionId(transactionId), HttpStatus.OK));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentResponseDTO>>> getPaymentByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                paymentService.getPaymentByUserId(userId), HttpStatus.OK));
    }
}

