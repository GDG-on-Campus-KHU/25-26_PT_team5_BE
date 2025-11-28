package com.gdg.team5.mail.dto;

import java.util.List;

public record NewsletterRequestDto(
    String userEmail,
    String userName,
    List<NewsEmailDto> newsList,
    List<JobEmailDto> jobsList
) {}
