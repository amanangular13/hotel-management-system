package com.amanverma.hotelmanagementsystem.user_service.service.impl;

import com.amanverma.hotelmanagementsystem.user_service.dto.UserRequestDTO;
import com.amanverma.hotelmanagementsystem.user_service.dto.UserResponseDTO;
import com.amanverma.hotelmanagementsystem.user_service.exception.DuplicateUserException;
import com.amanverma.hotelmanagementsystem.user_service.exception.UserNotFoundException;
import com.amanverma.hotelmanagementsystem.user_service.model.Role;
import com.amanverma.hotelmanagementsystem.user_service.model.User;
import com.amanverma.hotelmanagementsystem.user_service.repository.UserRepository;
import com.amanverma.hotelmanagementsystem.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
                .password(dto.getPassword())
                .role(Role.USER)
                .active(true)
                .build();

        userRepository.save(user);
        log.info("User created successfully");
        user.setPassword(null);
        log.info("Exit register()");
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getByEmail(String email) {
        log.info("Enter getByEmail()");
        log.info("Fetching user by email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User fetched Successfully");
        log.info("Exit getByEmail()");
        return modelMapper.map(user, UserResponseDTO.class);
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
}
