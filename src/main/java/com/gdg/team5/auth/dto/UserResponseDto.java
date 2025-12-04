package com.gdg.team5.auth.dto;

public record UserResponseDto(
    Long id,
    String email,
    String name
) {
}
