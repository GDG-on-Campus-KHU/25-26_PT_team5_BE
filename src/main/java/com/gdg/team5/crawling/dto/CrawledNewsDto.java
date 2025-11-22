package com.gdg.team5.crawling.dto;

import lombok.Data;

@Data
public class CrawledNewsDto {
    private String externalId;
    private String title;
    private String content;
    private String url;
    private String publishedDate;
    private String category;
    private String reporter;
    private String provider;
    private String thumbnailUrl;
}
