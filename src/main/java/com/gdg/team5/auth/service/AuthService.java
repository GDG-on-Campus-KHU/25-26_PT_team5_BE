package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.LoginRequest;
import com.gdg.team5.auth.dto.LoginResponse;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.auth.util.JwtUtil;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdg.team5.auth.dto.SignupRequest;
import com.gdg.team5.auth.dto.SignupResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    // final 필드 선언 위치 정리 (클래스 상단에서 한 번에 선언)
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //---------------------------
    // 1. 회원가입 기능 추가
    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BaseException(BaseResponseStatus.USER_EXIST_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .name(request.getName())
            .build();
            
        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.id(), "회원가입 성공");
    }


    //---------------------------
    // 로그인 기능
    public LoginResponse login(LoginRequest request) {
        
        // 1. 사용자 조회 및 예외 
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.UNAUTHORIZED_ERROR)); 
        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.password())) {
            throw new BaseException(BaseResponseStatus.UNAUTHORIZED_ERROR);
        }

        // 3. JWT 토큰 생성
        String token = jwtUtil.generateToken(user.email(), user.id());
        
        return new LoginResponse(token, user.id());
    }

    // 로그아웃 API 삭제 (프론트엔드 처리)
}