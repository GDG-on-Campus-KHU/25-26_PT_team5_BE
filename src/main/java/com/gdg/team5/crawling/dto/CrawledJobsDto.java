package com.gdg.team5.crawling.dto;

import lombok.Data;

@Data
public class CrawledJobsDto {
    private String externalId;
    private String title;
    private String companyName;
    private String content;
    private String url;
    private String postedDate;
    private String deadLine;
    private String category;
    private String tech_stack;
    private String location;
    private String exp_level;
    private String thumbnailUrl;
}
