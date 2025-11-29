package com.gdg.team5.scrap.dto;

import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.scrap.domain.Scrap;

public record ScrapJobResponseDto(
    // 공통 필드
    Long id,
    String source,
    String externalId,
    String title,
    String content,
    String url,
    String category,
    String thumbnailUrl,
    // 공고 전용 필드
    String companyName,
    String postedDate,
    String deadLine,
    String techStack,
    String location,
    String expLevel
) implements ScrapResponseDto {

    public static ScrapJobResponseDto from(Scrap scrap, JobPostings jobPosting) {
        return new ScrapJobResponseDto(
            scrap.getId(),
            jobPosting.getSource(),
            jobPosting.getExternalId(),
            jobPosting.getTitle(),
            jobPosting.getContent(),
            jobPosting.getUrl(),
            jobPosting.getCategory(),
            jobPosting.getThumbnailUrl(),
            jobPosting.getCompanyName(),
            jobPosting.getPostedDate(),
            jobPosting.getDeadLine(),
            jobPosting.getTechStack(),
            jobPosting.getLocation(),
            jobPosting.getExpLevel()
        );
    }
}
