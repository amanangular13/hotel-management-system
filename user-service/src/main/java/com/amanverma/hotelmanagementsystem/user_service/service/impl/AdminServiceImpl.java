package com.amanverma.hotelmanagementsystem.user_service.service.impl;

import com.amanverma.hotelmanagementsystem.user_service.dto.UserResponseDTO;
import com.amanverma.hotelmanagementsystem.user_service.exception.InvalidRolePromotionException;
import com.amanverma.hotelmanagementsystem.user_service.exception.UserNotFoundException;
import com.amanverma.hotelmanagementsystem.user_service.model.Role;
import com.amanverma.hotelmanagementsystem.user_service.model.User;
import com.amanverma.hotelmanagementsystem.user_service.repository.UserRepository;
import com.amanverma.hotelmanagementsystem.user_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponseDTO promoteUser(String email) {
        log.info("Enter promoteUser()");
        log.info("Fetching user by email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User fetched Successfully");
        if (user.getRole() != Role.USER) {
            throw new InvalidRolePromotionException("Only USER can be promoted");
        }

        user.setRole(Role.HOTEL_MANAGER);
        userRepository.save(user);
        log.info("User promoted to HOTEL_MANAGER successfully");
        user.setPassword(null);
        log.info("Exit promoteUser()");
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO deactivateUser(Long id) {
        log.info("Enter deactivateUser()");
        log.info("Fetching user by Id");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User fetched Successfully");
        user.setActive(false);
        userRepository.save(user);
        log.info("User deactivated successfully");
        user.setPassword(null);
        log.info("Exit deactivateUser()");
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
