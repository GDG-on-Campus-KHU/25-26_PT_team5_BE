package com.gdg.team5.auth.controller;

import com.gdg.team5.auth.dto.UserInfoResponse;
import com.gdg.team5.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 💡 추가
import org.springframework.security.core.userdetails.UserDetails; // 💡 또는 사용자의 Custom Principal Type
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 💡 수정된 GET /api/users/me
    @GetMapping("/me")
    // @AuthenticationPrincipal을 사용하여 인증된 사용자 정보 가져오기
    // Spring Security UserDetails 객체를 사용한다고 가정
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails principal) {

        // UserDetails 객체에서 인증된 사용자의 식별자(보통 username/email) 가져오기.
        // UserDetails의 getUsername() 메서드는 사용자의 ID 역할을 수행합니다.
        String authenticatedUserIdentifier = principal.getUsername(); 

        // 사용자 정보를 조회
        UserInfoResponse response = userService.getUserInfo(authenticatedUserIdentifier);
        return ResponseEntity.ok(response);
    }
}