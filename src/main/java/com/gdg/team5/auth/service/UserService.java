package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.UserInfoResponse;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus; // 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponse getUserInfo(String email) {
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        return UserInfoResponse.from(user);
    }
}