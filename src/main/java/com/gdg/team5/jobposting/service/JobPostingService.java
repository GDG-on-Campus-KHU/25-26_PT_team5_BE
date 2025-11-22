package com.gdg.team5.jobposting.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.jobposting.repository.JobPostingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingsRepository JobPostingsRepository;

    // 크롤링된 채용공고 저장 (업데이트 or 신규 생성)
    public void saveCrawledJobPostings(List<CrawledJobsDto> items) {

        for (CrawledJobsDto dto : items) {

            // 1. 기존 채용 공고 찾기
            JobPostings jobPostings = JobPostingsRepository.findByExternalId(dto.getExternalId())
                .orElse(new JobPostings());

            jobPostings.setExternalId(dto.getExternalId());
            jobPostings.setTitle(dto.getTitle());
            jobPostings.setCompanyName(dto.getCompanyName());
            jobPostings.setContent(dto.getContent());
            jobPostings.setUrl(dto.getUrl());
            jobPostings.setPostedDate(dto.getPostedDate());
            jobPostings.setDeadLine(dto.getDeadLine());
            jobPostings.setCategory(dto.getCategory());
            jobPostings.setTech_stack(dto.getTech_stack());
            jobPostings.setLocation(dto.getLocation());
            jobPostings.setExp_level(dto.getExp_level());
            jobPostings.setThumbnailUrl(dto.getThumbnailUrl());

            JobPostingsRepository.save(jobPostings);
        }

        log.info("크롤링된 채용공고 {}건 저장 완료", items.size());
    }

    // 유저 관심 채용공고 추천
//    public List<JobPostings> findRecommendedJobPostings(Preference pref) {
//
//    }
}

