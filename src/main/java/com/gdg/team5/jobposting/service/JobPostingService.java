package com.gdg.team5.jobposting.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.jobposting.repository.JobPostingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JobPostingService {
    private final JobPostingsRepository jobPostingsRepository;

    // 크롤링된 채용공고 저장 (업데이트 or 신규 생성)
    public void saveCrawledJobPostings(List<CrawledJobsDto> items) {

        if (items == null || items.isEmpty()) {
            log.info("크롤링된 채용공고 없음");
            return;
        }

        for (CrawledJobsDto dto : items) {

            // 1. 기존 채용 공고 찾기
            JobPostings jobPostings = jobPostingsRepository
                .findByExternalId(dto.externalId())
                .orElse(null);

            // 2. 없을 경우 신규 생성
            if (jobPostings == null) {
                jobPostings = JobPostings.builder()
                    .externalId(dto.externalId())
                    .title(dto.title())
                    .content(dto.content())
                    .postedDate(dto.postedDate())
                    .deadLine(dto.deadLine())
                    .category(dto.category())
                    .url(dto.url())
                    .location(dto.location())
                    .thumbnailUrl(dto.thumbnailUrl())
                    .companyName(dto.companyName())
                    .techStack(dto.techStack())
                    .expLevel(dto.expLevel())
                    .build();
            } else {
                jobPostings.updateFromJobsDto(dto);
            }

            jobPostingsRepository.save(jobPostings);
        }

        log.info("크롤링된 채용공고 {}건 저장 완료", items.size());
    }

    // 유저 관심 채용공고 추천
//    public List<JobPostings> findRecommendedJobPostings(Preference pref) {
//
//    }
}

