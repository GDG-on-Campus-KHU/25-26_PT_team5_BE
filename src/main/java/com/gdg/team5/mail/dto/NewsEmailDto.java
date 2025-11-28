package com.gdg.team5.mail.dto;

public record NewsEmailDto(
    String title,
    String content,
    String url,
    String thumbnailUrl,
    String category,
    String publishedDate
) {}
