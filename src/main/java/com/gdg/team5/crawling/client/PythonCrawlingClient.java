package com.gdg.team5.crawling.client;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonCrawlingClient {

    private final WebClient pythonWebClient;

    public List<CrawledNewsDto> reqeustNewsCrawling() {
        log.info("뉴스 크롤링 요청 to 파이썬 서버");

        return pythonWebClient.post()
            .uri("/crawl/news")
            .retrieve()
            .bodyToFlux(CrawledNewsDto.class)
            .collectList()
            .block();
    }

    public List<CrawledJobsDto> requestJobsCrawling() {
        log.info("채용공고 크롤링 요청 to 파이썬 서버");

        return pythonWebClient.post()
            .uri("/crawl/jobs")
            .retrieve()
            .bodyToFlux(CrawledJobsDto.class)
            .collectList()
            .block();
    }
}
