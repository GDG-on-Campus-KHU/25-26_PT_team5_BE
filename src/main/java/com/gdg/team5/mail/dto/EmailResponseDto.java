package com.gdg.team5.mail.dto;

public record EmailResponseDto(
    boolean success,
    String message,
    String recipientEmail,
    int newsCount,
    int jobsCount
) {
}
