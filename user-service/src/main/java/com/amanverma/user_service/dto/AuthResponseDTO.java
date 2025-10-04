package com.amanverma.user_service.dto;

import com.amanverma.user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private Boolean active;
}
