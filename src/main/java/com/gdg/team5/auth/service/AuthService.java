package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.LoginRequestDto;
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

    public String login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.UNAUTHORIZED_ERROR);
        }

        return jwtUtil.generateToken(user.getEmail(), user.getId());
    }
}
