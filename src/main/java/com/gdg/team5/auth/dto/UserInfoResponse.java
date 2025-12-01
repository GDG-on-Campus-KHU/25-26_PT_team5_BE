package com.gdg.team5.auth.dto;

import com.gdg.team5.auth.domain.User;


public record UserInfoResponse(
        Long id,
        String name,
        String email
) {
    
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}