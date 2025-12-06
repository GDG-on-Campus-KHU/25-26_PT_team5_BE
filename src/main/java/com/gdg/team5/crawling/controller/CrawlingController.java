package com.gdg.team5.crawling.controller;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.crawling.service.CrawlingRequestService;
import com.gdg.team5.mail.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawling")
public class CrawlingController {
    private final CrawlingRequestService crawlingRequestService;
    private final EmailService emailService;

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
    @PostMapping("/daily-batch")
    public BaseResponse<String> runDailyBatch() {
        log.info("=== 🌞 일일 배치 작업(크롤링+메일) 시작 ===");

        // 1단계: 뉴스 가져오기
        crawlingRequestService.crawlNewsAndSave();

        // 2단계: 채용공고 가져오기
        crawlingRequestService.crawlJobsAndSave();

        // 3단계: 이메일 보내기 (구현하신 메서드명에 맞춰주세요!)
        // 예: 전체 유저에게 보내는 로직이 있다면 여기서 호출
        // EmailService.sendNewsletterToAllUsers();
        emailService.sendNewsletterToAllUsers();

        log.info("=== ✅ 일일 배치 작업 완료 ===");
        return new BaseResponse<>("일일 배치 작업(뉴스+채용+이메일) 완료");
    }
}
