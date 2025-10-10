package com.amanverma.hotelmanagementsystem.auth_service.feign;

import com.amanverma.hotelmanagementsystem.auth_service.advice.ApiResponse;
import com.amanverma.hotelmanagementsystem.auth_service.dto.RegisterRequestDTO;
import com.amanverma.hotelmanagementsystem.auth_service.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/users")
public interface UserClient {

    @GetMapping(path = "/email/{email}", produces = "application/json")
    ApiResponse<UserResponseDTO> getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/register")
    ApiResponse<UserResponseDTO> register(@RequestBody RegisterRequestDTO request);

}
