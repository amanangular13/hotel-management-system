package com.amanverma.hotelmanagementsystem.auth_service.dto;

import com.amanverma.hotelmanagementsystem.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {
    private String name;
    private String email;
    private String phone;
    private Role role;
}
