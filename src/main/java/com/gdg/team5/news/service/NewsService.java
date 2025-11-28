package com.gdg.team5.news.service;

import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.news.domain.News;
import com.gdg.team5.news.repository.NewsRepository;
import com.gdg.team5.preference.domain.Preference;
import com.gdg.team5.preference.domain.UserPreference;
import com.gdg.team5.preference.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NewsService {
    private final NewsRepository newsRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    // 크롤링된 뉴스 저장 (업데이트 or 신규 생성)
    public void saveCrawledNews(List<CrawledNewsDto> items) {
        if (items == null || items.isEmpty()) {
            log.info("크롤링된 뉴스 없음");
            return;
        }

        for (CrawledNewsDto dto : items) {

            // 1. 기존 뉴스 찾기
            News news = newsRepository
                .findBySourceAndExternalId(dto.source(), dto.externalId())
                .orElse(null);

            // 2. 데이터 업데이트
            if (news == null) {
                news = News.builder()
                    .source(dto.source())
                    .externalId(dto.externalId())
                    .title(dto.title())
                    .url(dto.url())
                    .publishedDate(dto.publishedDate())
                    .content(dto.content())
                    .reporter(dto.reporter())
                    .provider(dto.provider())
                    .thumbnailUrl(dto.thumbnailUrl())
                    .content(dto.content())
                    .build();
            } else {
                news.updateFromNewsDto(dto);
            }

            newsRepository.save(news);

        }

        log.info("크롤링된 뉴스 {}건 저장 완료", items.size());
    }

    // 유저 관심 뉴스 추천
    @Transactional(readOnly = true)
    public List<News> findRecommendedNewsByUserId(Long userId) {
        List<UserPreference> userPreferences = userPreferenceRepository.findAllByUserIdWithPreference(userId);

        Set<News> recommended = new LinkedHashSet<>();

        for (UserPreference userPreference : userPreferences) {
            Preference pref = userPreference.getPreference();
            if (pref == null) {
                continue;
            }
            String keyword = pref.getKeyword();
            if (keyword == null || keyword.isBlank()) {
                continue;
            }

            List<News> newsList = newsRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                keyword, keyword
            );

            recommended.addAll(newsList);
        }

        return new ArrayList<>(recommended);
    }
}

