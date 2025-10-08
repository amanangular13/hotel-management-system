package com.amanverma.hotelmanagementsystem.loyalty_service.service.Impl;

import com.amanverma.hotelmanagementsystem.loyalty_service.dto.LoyaltyRequestDTO;
import com.amanverma.hotelmanagementsystem.loyalty_service.dto.LoyaltyResponseDTO;
import com.amanverma.hotelmanagementsystem.loyalty_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.loyalty_service.model.Loyalty;
import com.amanverma.hotelmanagementsystem.loyalty_service.repository.LoyaltyRepository;
import com.amanverma.hotelmanagementsystem.loyalty_service.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {

    private final LoyaltyRepository loyaltyRepository;
    private final ModelMapper modelMapper;

    @Override
    public LoyaltyResponseDTO addPoints(LoyaltyRequestDTO requestDTO) {
        Loyalty loyalty = loyaltyRepository.findByUserId(requestDTO.getUserId())
                .orElse(Loyalty.builder()
                        .userId(requestDTO.getUserId())
                        .points(0)
                        .build());

        loyalty.setPoints(loyalty.getPoints() + requestDTO.getPoints());
        Loyalty saved = loyaltyRepository.save(loyalty);

        return modelMapper.map(saved, LoyaltyResponseDTO.class);
    }

    @Override
    public LoyaltyResponseDTO redeemPoints(LoyaltyRequestDTO requestDTO) {
        Loyalty loyalty = loyaltyRepository.findByUserId(requestDTO.getUserId())
                .orElseThrow(() -> new ApiException("User loyalty record not found", HttpStatus.NOT_FOUND));

        if (loyalty.getPoints() < requestDTO.getPoints()) {
            throw new ApiException("Insufficient loyalty points", HttpStatus.BAD_REQUEST);
        }

        loyalty.setPoints(loyalty.getPoints() - requestDTO.getPoints());
        Loyalty saved = loyaltyRepository.save(loyalty);

        return modelMapper.map(saved, LoyaltyResponseDTO.class);
    }

    @Override
    public LoyaltyResponseDTO getPointsByUserId(Long userId) {
        Loyalty loyalty = loyaltyRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("User loyalty record not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(loyalty, LoyaltyResponseDTO.class);
    }
}

