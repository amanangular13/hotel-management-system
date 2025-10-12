package com.amanverma.hotelmanagementsystem.payment_service.service.impl;

import com.amanverma.hotelmanagementsystem.payment_service.dto.PaymentRequestDTO;
import com.amanverma.hotelmanagementsystem.payment_service.dto.PaymentResponseDTO;
import com.amanverma.hotelmanagementsystem.payment_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.payment_service.feign.LoyaltyClient;
import com.amanverma.hotelmanagementsystem.payment_service.model.Payment;
import com.amanverma.hotelmanagementsystem.payment_service.model.enums.PaymentStatus;
import com.amanverma.hotelmanagementsystem.payment_service.repository.PaymentRepository;
import com.amanverma.hotelmanagementsystem.payment_service.service.PaymentService;
import com.amanverma.hotelmanagementsystem.payment_service.util.PaymentGatewaySimulator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewaySimulator paymentGatewaySimulator;
    private final ModelMapper modelMapper;
    private final LoyaltyClient loyaltyClient;

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        try {
            int loyaltyPointsUsed = 0;
            double discount = 0.0;
            if (Boolean.TRUE.equals(request.getUsedLoyaltyPoints())) {
                loyaltyPointsUsed = loyaltyClient.getPoints(request.getUserId()).getData().getPoints();
                discount = loyaltyPointsUsed * 0.5;
            }

            double finalAmount = request.getAmount() - discount;
            String transactionId = UUID.randomUUID().toString();

            boolean success = paymentGatewaySimulator.processTransaction(transactionId, finalAmount);

            PaymentStatus status = success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

            Payment payment = Payment.builder()
                    .bookingId(request.getBookingId())
                    .userId(request.getUserId())
                    .amount(request.getAmount())
                    .finalAmountCharged(finalAmount)
                    .status(status)
                    .paymentMethod(request.getPaymentMethod())
                    .transactionId(transactionId)
                    .usedLoyaltyPoints(request.getUsedLoyaltyPoints())
                    .loyaltyPointsUsed(loyaltyPointsUsed)
                    .build();

            paymentRepository.save(payment);

            return PaymentResponseDTO.builder()
                    .id(payment.getId())
                    .bookingId(payment.getBookingId())
                    .amount(payment.getAmount())
                    .status(payment.getStatus())
                    .paymentMethod(payment.getPaymentMethod())
                    .transactionId(payment.getTransactionId())
                    .usedLoyaltyPoints(payment.getUsedLoyaltyPoints())
                    .loyaltyPointsUsed(payment.getLoyaltyPointsUsed())
                    .finalAmountCharged(payment.getFinalAmountCharged())
                    .createdAt(payment.getCreatedAt())
                    .build();

        } catch (Exception e) {
            throw new ApiException("Payment processing failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Payment not found", HttpStatus.NOT_FOUND));

        return modelMapper.map(payment, PaymentResponseDTO.class);
    }

    @Override
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ApiException("Transaction not found", HttpStatus.NOT_FOUND));

        return modelMapper.map(payment, PaymentResponseDTO.class);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentByUserId(Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(booking -> modelMapper.map(booking, PaymentResponseDTO.class))
                .collect(Collectors.toList());
    }
}