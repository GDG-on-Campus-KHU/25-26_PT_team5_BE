package com.gdg.team5.mail.dto;

public record NewsletterResponseDto(
    boolean success,
    String message,
    String recipientEmail,
    int newsCount,
    int jobsCount
) {}
