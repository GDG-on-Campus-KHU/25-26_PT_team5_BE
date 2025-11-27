package com.gdg.team5.auth.controller;

import com.gdg.team5.auth.dto.LogoutResponse;
import com.gdg.team5.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        authService.logout(request);

        LogoutResponse response = new LogoutResponse(true, "로그아웃 되었습니다.");
        return ResponseEntity.ok(response);
    }
}
