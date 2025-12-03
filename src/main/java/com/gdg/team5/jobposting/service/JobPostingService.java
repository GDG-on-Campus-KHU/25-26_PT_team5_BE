package com.gdg.team5.jobposting.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.jobposting.repository.JobPostingsRepository;
import com.gdg.team5.preference.domain.Preference;
import com.gdg.team5.preference.domain.UserPreference;
import com.gdg.team5.preference.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JobPostingService {
    private final JobPostingsRepository jobPostingsRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    // 크롤링된 채용공고 저장 (업데이트 or 신규 생성)
    public void saveCrawledJobPostings(List<CrawledJobsDto> items) {

        if (items == null || items.isEmpty()) {
            log.info("크롤링된 채용공고 없음");
            return;
        }

        for (CrawledJobsDto dto : items) {

            // 1. 기존 채용 공고 찾기
            JobPostings jobPostings = jobPostingsRepository
                .findBySourceAndExternalId(dto.source(), dto.externalId())
                .orElse(null);

            // 2. 없을 경우 신규 생성
            if (jobPostings == null) {
                jobPostings = JobPostings.builder()
                    .source(dto.source())
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
    public List<JobPostings> findRecommendedJobPostingsByUserId(Long userId) {

        List<UserPreference> userPreferences = userPreferenceRepository.findAllByUserIdWithPreference(userId);

        // 새로운 공고 기준 날짜: 오늘 기준 1일 전에 올라온 공고만 새로운 공고로 취급
        String fromDate = LocalDate.now().minusDays(1).toString();

        Set<JobPostings> recommended = new LinkedHashSet<>();

        for (UserPreference userPreference : userPreferences) {
            Preference preference = userPreference.getPreference();
            if (preference == null) {
                continue;
            }

            String keyword = preference.getKeyword();
            if (keyword == null || keyword.isBlank()) {
                continue;
            }

            List<JobPostings> jobPostingsList = jobPostingsRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrTechStackContainingIgnoreCase(
                keyword,
                keyword,
                keyword
            );

            for (JobPostings job : jobPostingsList) {
                if (isNewJob(job, fromDate)) {
                    recommended.add(job);
                }
            }
        }
        return new ArrayList<>(recommended);
    }

    private boolean isNewJob(JobPostings job, String fromDate) {
        String postedDate = job.getPostedDate();
        if (postedDate == null || postedDate.isBlank()) {
            return false;
        }
        return postedDate.compareTo(fromDate) >= 0;
    }
}

