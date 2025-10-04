package com.amanverma.auth_service.feign;

import com.amanverma.auth_service.advice.ApiResponse;
import com.amanverma.auth_service.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/users")
public interface UserClient {

    @GetMapping(path = "/email/{email}", produces = "application/json")
    ApiResponse<UserResponseDTO> getUserByEmail(@PathVariable("email") String email);
}
