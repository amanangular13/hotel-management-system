package com.amanverma.user_service.service;

import com.amanverma.user_service.dto.AuthResponseDTO;
import com.amanverma.user_service.dto.UserRequestDTO;
import com.amanverma.user_service.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register(UserRequestDTO dto);

    AuthResponseDTO getByEmail(String email);

    UserResponseDTO getById(Long id);

    UserResponseDTO updateProfile(Long id, UserRequestDTO dto);

    UserResponseDTO promoteUser(String email);

    UserResponseDTO deactivateUser(Long id);
}

