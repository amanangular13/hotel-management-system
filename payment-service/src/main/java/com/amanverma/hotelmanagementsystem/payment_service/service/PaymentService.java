package com.amanverma.hotelmanagementsystem.payment_service.service;

import com.amanverma.hotelmanagementsystem.payment_service.dto.PaymentRequestDTO;
import com.amanverma.hotelmanagementsystem.payment_service.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO request);
    PaymentResponseDTO getPaymentById(Long id);
    PaymentResponseDTO getPaymentByTransactionId(String transactionId);
}

