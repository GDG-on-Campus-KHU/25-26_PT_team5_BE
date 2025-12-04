package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.LoginRequest;
import com.gdg.team5.auth.dto.LoginResponse;
import com.gdg.team5.auth.dto.SignupRequestDto;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.auth.util.JwtUtil;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new BaseException(BaseResponseStatus.USER_EXIST_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(requestDto.password());
        User user = User.builder()
            .email(requestDto.email())
            .password(encodedPassword)
            .name(requestDto.name())
            .build();
        userRepository.save(user);
    }


    //---------------------------
    // 로그인 기능
    public LoginResponse login(LoginRequest request) {

        // 1. 사용자 조회 및 예외
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.UNAUTHORIZED_ERROR));
        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.UNAUTHORIZED_ERROR);
        }

        // 3. JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return new LoginResponse(token, user.getId());
    }

    // 로그아웃 API 삭제 (프론트엔드 처리)
}
