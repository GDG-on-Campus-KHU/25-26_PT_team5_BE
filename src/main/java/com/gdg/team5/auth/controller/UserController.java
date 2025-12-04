package com.gdg.team5.auth.controller;

import com.gdg.team5.auth.domain.CustomUserDetails;
import com.gdg.team5.auth.dto.UserResponseDto;
import com.gdg.team5.auth.service.UserService;
import com.gdg.team5.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public BaseResponse<UserResponseDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(userService.getUserInfo(userDetails.getId()));
    }
}
