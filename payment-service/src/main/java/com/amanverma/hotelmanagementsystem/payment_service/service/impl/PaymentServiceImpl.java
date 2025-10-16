package com.amanverma.hotelmanagementsystem.payment_service.service.impl;

import com.amanverma.hotelmanagementsystem.payment_service.dto.LoyaltyDTO;
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
            int loyaltyPoints = 0;
            double discount = 0.0;
            if (Boolean.TRUE.equals(request.getUseLoyaltyPoints())) {
                loyaltyPoints = loyaltyClient.getPoints(request.getUserId()).getData().getPoints();
                discount = loyaltyPoints * 0.5;
            }

            double finalAmount = request.getAmount() - (int)discount;
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
                    .useLoyaltyPoints(request.getUseLoyaltyPoints())
                    .loyaltyPointsUsed((int)discount)
                    .build();

            paymentRepository.save(payment);

            if(success && request.getUseLoyaltyPoints()) {
                LoyaltyDTO redeem = LoyaltyDTO.builder()
                        .userId(request.getUserId())
                        .points((int)discount)
                        .build();
                loyaltyClient.redeemPoints(redeem);

                int awardPoint = (int) (finalAmount * 0.05);
                LoyaltyDTO add = LoyaltyDTO.builder()
                        .userId(request.getUserId())
                        .points(awardPoint)
                        .build();
                loyaltyClient.addPoints(add);
            }

            return PaymentResponseDTO.builder()
                    .id(payment.getId())
                    .bookingId(payment.getBookingId())
                    .amount(payment.getAmount())
                    .status(payment.getStatus())
                    .paymentMethod(payment.getPaymentMethod())
                    .transactionId(payment.getTransactionId())
                    .useLoyaltyPoints(payment.getUseLoyaltyPoints())
                    .loyaltyPointsUsed(payment.getLoyaltyPointsUsed())
                    .finalAmountCharged(payment.getFinalAmountCharged())
                    .createdAt(payment.getCreatedAt())
                    .build();

        } catch (Exception e) {
            throw new ApiException("Payment processing failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO refund(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ApiException("Transaction not found", HttpStatus.NOT_FOUND));

        if(payment.getUseLoyaltyPoints()) {
            LoyaltyDTO redeemed = LoyaltyDTO.builder()
                    .userId(payment.getUserId())
                    .points(payment.getLoyaltyPointsUsed())
                    .build();
            loyaltyClient.addPoints(redeemed);

            LoyaltyDTO added = LoyaltyDTO.builder()
                    .userId(payment.getUserId())
                    .points((int) (payment.getFinalAmountCharged() * 0.05))
                    .build();
            loyaltyClient.redeemPoints(added);

        }
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment saved = paymentRepository.save(payment);
        return modelMapper.map(saved, PaymentResponseDTO.class);
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