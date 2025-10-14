package com.amanverma.hotelmanagementsystem.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserClient {
    @GetMapping("/{userId}/exists")
    Boolean userExists(@PathVariable Long userId);
}
