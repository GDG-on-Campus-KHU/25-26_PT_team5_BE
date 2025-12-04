package com.gdg.team5.auth.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.dto.UserResponseDto;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        return new UserResponseDto(user.getId(), user.getEmail(), user.getName());
    }
}
