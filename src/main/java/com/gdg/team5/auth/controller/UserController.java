package com.gdg.team5.auth.controller;

import com.gdg.team5.auth.dto.UserInfoResponse;
import com.gdg.team5.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// ❌ 이거 이제 필요 없음
// import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 임시: 쿼리 파라미터로 email 받기
    // GET /api/users/me?email=aaa@bbb.com
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@RequestParam String email) {

        UserInfoResponse response = userService.getUserInfo(email);
        return ResponseEntity.ok(response);
    }
}
