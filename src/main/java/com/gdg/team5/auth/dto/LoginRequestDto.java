package com.gdg.team5.auth.dto;

public record LoginRequestDto(
    String email,
    String password
) {
}

