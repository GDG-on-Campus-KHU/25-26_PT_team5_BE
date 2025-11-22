package com.gdg.team5.news.service;

import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.news.domain.News;
import com.gdg.team5.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    // 크롤링된 뉴스 저장 (업데이트 or 신규 생성)
    public void saveCrawledNews(List<CrawledNewsDto> items) {

        for (CrawledNewsDto dto : items) {

            // 1. 기존 뉴스 찾기
            News news = newsRepository.findByExternalId(dto.getExternalId())
                .orElse(new News());

            // 2. 데이터 업데이트
            news.setExternalId(dto.getExternalId());
            news.setTitle(dto.getTitle());
            news.setContent(dto.getContent());
            news.setUrl(dto.getUrl());
            news.setPublishedDate(dto.getPublishedDate());
            news.setCategory(dto.getCategory());
            news.setReporter(dto.getReporter());
            news.setProvider(dto.getProvider());
            news.setThumbnailUrl(dto.getThumbnailUrl());

            newsRepository.save(news);

        }

        log.info("크롤링된 뉴스 {}건 저장 완료", items.size());
    }

    // 유저 관심 뉴스 추천
//    public List<News> findRecommendedNews(Preference pref) {
//
//    }

}
