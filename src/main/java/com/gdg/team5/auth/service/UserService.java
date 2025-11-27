package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.UserInfoResponse;
import com.gdg.team5.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return UserInfoResponse.from(user);
    }
}
