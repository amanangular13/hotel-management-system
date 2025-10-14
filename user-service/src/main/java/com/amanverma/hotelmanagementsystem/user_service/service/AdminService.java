package com.amanverma.hotelmanagementsystem.user_service.service;

import com.amanverma.hotelmanagementsystem.user_service.dto.UserResponseDTO;

import java.util.List;

public interface AdminService {
    UserResponseDTO promoteUser(String email);

    UserResponseDTO deactivateUser(Long id);

    List<UserResponseDTO> getRequestList();
}
