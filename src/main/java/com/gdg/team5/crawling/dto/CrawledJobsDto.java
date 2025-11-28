package com.gdg.team5.crawling.dto;


public record CrawledJobsDto(
    String externalId,
    String title,
    String companyName,
    String content,
    String url,
    String postedDate,
    String deadLine,
    String category,
    String techStack,
    String location,
    String expLevel,
    String thumbnailUrl
) {
};
