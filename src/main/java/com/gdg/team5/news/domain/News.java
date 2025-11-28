package com.gdg.team5.news.domain;

import com.gdg.team5.crawling.dto.CrawledNewsDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_news_source_externalId",
            columnNames = {"source", "externalId"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String externalId;

    private String title;
    private String url;
    private String publishedDate;
    private String category;
    private String reporter;
    private String provider;
    private String thumbnailUrl;
    @Lob
    private String content;

    @Builder
    public News(String source, String externalId, String title, String url,
                String publishedDate, String category, String reporter,
                String provider, String thumbnailUrl, String content) {
        this.source = source;
        this.externalId = externalId;
        this.title = title;
        this.url = url;
        this.publishedDate = publishedDate;
        this.category = category;
        this.reporter = reporter;
        this.provider = provider;
        this.thumbnailUrl = thumbnailUrl;
        this.content = content;
    }

    public void updateFromNewsDto(CrawledNewsDto crawledNewsDto) {
        this.source = crawledNewsDto.source();
        this.externalId = crawledNewsDto.externalId();
        this.title = crawledNewsDto.title();
        this.url = crawledNewsDto.url();
        this.publishedDate = crawledNewsDto.publishedDate();
        this.category = crawledNewsDto.category();
        this.reporter = crawledNewsDto.reporter();
        this.provider = crawledNewsDto.provider();
        this.thumbnailUrl = crawledNewsDto.thumbnailUrl();
        this.content = crawledNewsDto.content();
    }

}
