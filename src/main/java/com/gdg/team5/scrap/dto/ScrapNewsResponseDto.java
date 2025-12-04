package com.gdg.team5.scrap.dto;

import com.gdg.team5.news.domain.News;
import com.gdg.team5.scrap.domain.Scrap;

public record ScrapNewsResponseDto(
    // 공통 필드
    Long id,
    String source,
    String externalId,
    String title,
    String content,
    String url,
    String category,
    String thumbnailUrl,
    // 뉴스 전용 필드
    String publishedDate,
    String reporter,
    String provider
) implements ScrapResponseDto {

    public static ScrapNewsResponseDto from(Scrap scrap, News news) {
        return new ScrapNewsResponseDto(
            scrap.getId(),
            news.getSource(),
            news.getExternalId(),
            news.getTitle(),
            news.getContent(),
            news.getUrl(),
            news.getCategory(),
            news.getThumbnailUrl(),
            news.getPublishedDate(),
            news.getReporter(),
            news.getProvider()
        );
    }
}
