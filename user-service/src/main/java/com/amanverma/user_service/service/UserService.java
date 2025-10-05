package com.amanverma.user_service.service;

import com.amanverma.user_service.dto.UserRequestDTO;
import com.amanverma.user_service.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register(UserRequestDTO dto);

    UserResponseDTO getByEmail(String email);

    UserResponseDTO getById(Long id);

    UserResponseDTO updateProfile(Long id, UserRequestDTO dto);
}

