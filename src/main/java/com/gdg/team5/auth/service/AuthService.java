package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.LoginRequest;
import com.gdg.team5.auth.dto.LoginResponse;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gdg.team5.auth.dto.SignupRequest;
import com.gdg.team5.auth.dto.SignupResponse;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AuthService {
    //---------------------------
    //1. 회원가입 기능 추가
    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일");
        }


        String encodedPassword = passwordEncoder.encode(request.getPassword());


        User user = new User(request.getEmail(), encodedPassword, request.getName());
        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.getId(), "회원가입 성공");
    }


    //---------------------------
    //로그인 기능
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("인증 실패"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("인증 실패");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new LoginResponse(token, user.getId());
    }

    // TODO: 필요하면 JwtProvider, TokenBlacklistService 등을 주입해서 사용

    public void logout(HttpServletRequest request) {
        // 1. 헤더에서 토큰 뽑기
        String authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            // 로그아웃 요청이지만 토큰이 없으면 그냥 무시 (에러 처리하고 싶으면 커스텀 예외 던져도 됨)
            return;
        }

        String accessToken = authorizationHeader.substring(7);

        // 2. TODO: JWT 유효성 검증 (원래 쓰고 있던 JwtProvider 있으면 여기서 validate)
        // jwtProvider.validateToken(accessToken);

        // 3. TODO: 블랙리스트/로그아웃 처리 (Redis or DB)
        // tokenBlacklistService.add(accessToken);

        // 서버 기억 X
        // 프론트에서 토큰을 지우는 방식의 로그아웃
    }

}


