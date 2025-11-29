package com.gdg.team5.crawling.service;

import com.gdg.team5.crawling.client.PythonCrawlingClient;
import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.jobposting.service.JobPostingService;
import com.gdg.team5.news.service.NewsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingRequestService {

    private final PythonCrawlingClient pythonCrawlingClient;
    private final NewsService newsService;
    private final JobPostingService jobPostingService;

    // 뉴스 크롤링 & 저장
    public void crawlNewsAndSave() {
        log.info("뉴스 크롤링 & 저장 시작");

        List<CrawledNewsDto> items = pythonCrawlingClient.requestNewsCrawling();
        log.info("뉴스 {}건 수신", items.size());

        newsService.saveCrawledNews(items);
        log.info("뉴스 저장 완료");
    }

    public void crawlJobsAndSave() {
        log.info("채용공고 크롤링 & 저장 시작");

        List<CrawledJobsDto> items = pythonCrawlingClient.requestJobsCrawling();
        log.info("채용공고 {}건 수신", items.size());

        jobPostingService.saveCrawledJobPostings(items);
        log.info("뉴스 저장 완료");
    }

}
