package com.amanverma.hotelmanagementsystem.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean requestedForHotelManager;
}
