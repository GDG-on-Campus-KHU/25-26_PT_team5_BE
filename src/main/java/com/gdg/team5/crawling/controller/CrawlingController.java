package com.gdg.team5.crawling.controller;

import com.gdg.team5.crawling.service.CrawlingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawling")
public class CrawlingController {
    private final CrawlingRequestService crawlingRequestService;

    @PostMapping("/news")
    public ResponseEntity<?> triggerNewsCrawling() {
        crawlingRequestService.crawlNewsAndSave();
        return ResponseEntity.ok(
            Map.of(
                "status", 200,
                "message", "뉴스 크롤링 & 저장 요청 완료"
            )
        );
    }

    @PostMapping("/jobs")
    public ResponseEntity<?> triggerJobsCrawling() {
        crawlingRequestService.crawlJobsAndSave();
        return ResponseEntity.ok(
            Map.of(
                "status", 200,
                "message", "채용공고 크롤링 & 저장 요청 완료"
            )
        );
    }
}
