package com.gdg.team5.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

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
