package com.gdg.team5.auth.dto;

public record SignupRequestDto(
    String email,
    String password,
    String name
) {
}
