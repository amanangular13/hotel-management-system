package com.amanverma.hotelmanagementsystem.loyalty_service.service;

import com.amanverma.hotelmanagementsystem.loyalty_service.dto.LoyaltyRequestDTO;
import com.amanverma.hotelmanagementsystem.loyalty_service.dto.LoyaltyResponseDTO;

public interface LoyaltyService {

    LoyaltyResponseDTO addPoints(LoyaltyRequestDTO requestDTO);

    LoyaltyResponseDTO redeemPoints(LoyaltyRequestDTO requestDTO);

    LoyaltyResponseDTO getPointsByUserId(Long userId);
}

