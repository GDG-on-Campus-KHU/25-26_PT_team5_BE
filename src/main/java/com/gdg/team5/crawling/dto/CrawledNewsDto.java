package com.gdg.team5.crawling.dto;

public record CrawledNewsDto(
    String externalId,
    String title,
    String content,
    String url,
    String publishedDate,
    String category,
    String reporter,
    String provider,
    String thumbnailUrl
) {
}
