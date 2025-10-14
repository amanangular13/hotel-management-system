package com.amanverma.hotelmanagementsystem.booking_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private String transactionId;
    private Boolean success;
    private Double amount;
}
