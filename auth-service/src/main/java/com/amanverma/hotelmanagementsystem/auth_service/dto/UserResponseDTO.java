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
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private boolean active;
}
