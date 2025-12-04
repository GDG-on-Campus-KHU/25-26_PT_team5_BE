package com.gdg.team5.auth.controller;

import com.gdg.team5.auth.dto.LoginRequest;
import com.gdg.team5.auth.dto.LoginResponse;
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
    public BaseResponse<String> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return new BaseResponse<>("회원가입에 성공했습니다.");
    }

    // ----------------------------
    // 2. 로그인 기능
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        // BaseResponse 성공 응답 반환
        return new BaseResponse<LoginResponse>(response);
    }

    ////service에서 logout 메서드를 삭제했으면 controller에서도 삭제해야함
//    // ----------------------------
//    //3. 로그아웃 기능
//    @PostMapping("/logout")
//    public BaseResponse<LogoutResponse> logout(HttpServletRequest request) {
//        authService.logout(request);
//
//        LogoutResponse response = new LogoutResponse(true, "로그아웃 되었습니다.");
//
//        // BaseResponse 성공 응답 반환
//        return new BaseResponse<LogoutResponse>(response);
//    }
//

}
