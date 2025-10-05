package com.amanverma.user_service.service;

import com.amanverma.user_service.dto.UserResponseDTO;

public interface AdminService {
    UserResponseDTO promoteUser(String email);

    UserResponseDTO deactivateUser(Long id);
}
