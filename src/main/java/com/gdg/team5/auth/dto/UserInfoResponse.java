package com.gdg.team5.auth.dto;

import com.gdg.team5.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String name;
    private String email;

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
