package com.gdg.team5.jobposting.domain;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_jobs_source_externalId",
            columnNames = {"source", "externalId"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPostings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String externalId;

    private String title;
    private String companyName;
    private String url;
    private String postedDate;
    private String deadLine;
    private String category;
    private String techStack;
    private String location;
    private String expLevel;
    private String thumbnailUrl;

    @Lob
    private String content;

    @Builder
    public JobPostings(String source, String externalId, String title, String companyName,
                       String url, String postedDate, String deadLine,
                       String category, String techStack, String location,
                       String expLevel, String thumbnailUrl, String content) {
        this.source = source;
        this.externalId = externalId;
        this.title = title;
        this.companyName = companyName;
        this.url = url;
        this.postedDate = postedDate;
        this.deadLine = deadLine;
        this.category = category;
        this.techStack = techStack;
        this.location = location;
        this.expLevel = expLevel;
        this.thumbnailUrl = thumbnailUrl;
        this.content = content;
    }


    public void updateFromJobsDto(CrawledJobsDto crawledJobsDto) {
        this.source = crawledJobsDto.source();
        this.externalId = crawledJobsDto.externalId();
        this.title = crawledJobsDto.title();
        this.companyName = crawledJobsDto.companyName();
        this.content = crawledJobsDto.content();
        this.url = crawledJobsDto.url();
        this.postedDate = crawledJobsDto.postedDate();
        this.deadLine = crawledJobsDto.deadLine();
        this.category = crawledJobsDto.category();
        this.techStack = crawledJobsDto.techStack();
        this.location = crawledJobsDto.location();
        this.expLevel = crawledJobsDto.expLevel();
        this.thumbnailUrl = crawledJobsDto.thumbnailUrl();
    }
}
