package com.gdg.team5.crawling.controller;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.crawling.service.CrawlingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawling")
public class CrawlingController {
    private final CrawlingRequestService crawlingRequestService;

    @PostMapping("/news")
    public BaseResponse<String> triggerNewsCrawling() {
        crawlingRequestService.crawlNewsAndSave();
        return new BaseResponse<>("뉴스 크롤링 & 저장 요청 완료");
    }

    @PostMapping("/jobs")
    public BaseResponse<String> triggerJobsCrawling() {
        crawlingRequestService.crawlJobsAndSave();
        return new BaseResponse<>("채용공고 크롤링 & 저장 요청 완료");
    }
}
