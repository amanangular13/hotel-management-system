package com.amanverma.hotelmanagementsystem.payment_service.util;

import org.springframework.stereotype.Component;

@Component
public class PaymentGatewaySimulator {

    public boolean processTransaction(String transactionId, double amount) {
        return Math.random() > 0.1;
    }
}

