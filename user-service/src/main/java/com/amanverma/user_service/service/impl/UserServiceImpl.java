package com.amanverma.user_service.service.impl;

import com.amanverma.user_service.dto.AuthResponseDTO;
import com.amanverma.user_service.dto.UserRequestDTO;
import com.amanverma.user_service.dto.UserResponseDTO;
import com.amanverma.user_service.exception.InvalidRolePromotionException;
import com.amanverma.user_service.exception.DuplicateUserException;
import com.amanverma.user_service.exception.UserNotFoundException;
import com.amanverma.user_service.model.Role;
import com.amanverma.user_service.model.User;
import com.amanverma.user_service.repository.UserRepository;
import com.amanverma.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO register(UserRequestDTO dto) {
        log.info("Enter register()");
        log.info("starting registration");

        log.info("Checking if email exists");
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateUserException("Email already exists");
        }
        log.info("Email doesn't exists");

        log.info("Checking if phone exists");
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateUserException("Phone already exists");
        }
        log.info("Phone doesn't exists");
        log.info("Creating User");
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();

        userRepository.save(user);
        log.info("User created successfully");
        log.info("Exit register()");
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public AuthResponseDTO getByEmail(String email) {
        log.info("Enter getByEmail()");
        log.info("Fetching user by email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User fetched Successfully");
        log.info("Exit getByEmail()");
        return modelMapper.map(user, AuthResponseDTO.class);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        log.info("Enter getById()");
        log.info("Fetching user by Id");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User fetched Successfully");
        log.info("Exit getById()");
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO updateProfile(Long id, UserRequestDTO dto) {
        log.info("Enter updateProfile()");
        log.info("Fetching user by Id");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User fetched Successfully");

        log.info("Checking if phone exists");
        if(userRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateUserException("Phone already exists");
        }
        log.info("Phone doesn't exist, proceeding further");
        user.setPhone(dto.getPhone());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        userRepository.save(user);
        log.info("User profile update successfully");
        log.info("Exit updateProfile()");
        return modelMapper.map(user, UserResponseDTO.class);
    }

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
        log.info("Exit deactivateUser()");
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
