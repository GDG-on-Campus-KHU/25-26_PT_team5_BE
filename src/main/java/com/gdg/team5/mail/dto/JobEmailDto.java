package com.gdg.team5.mail.dto;

public record JobEmailDto(
    String title,
    String companyName,
    String content,
    String url,
    String thumbnailUrl,
    String location,
    String deadLine
) {}
