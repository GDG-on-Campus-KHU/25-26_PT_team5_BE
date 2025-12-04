package com.gdg.team5.auth.controller;

import com.gdg.team5.auth.dto.LoginRequestDto;
import com.gdg.team5.auth.dto.SignupRequestDto;
import com.gdg.team5.auth.service.AuthService;
import com.gdg.team5.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public BaseResponse<String> signup(@RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return new BaseResponse<>("회원가입에 성공했습니다.");
    }

    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody LoginRequestDto requestDto) {
        return new BaseResponse<>(authService.login(requestDto));
    }
}
