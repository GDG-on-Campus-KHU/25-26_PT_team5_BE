package com.gdg.team5.mail.controller;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.crawling.service.CrawlingService;
import com.gdg.team5.mail.dto.EmailResponseDto;
import com.gdg.team5.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final CrawlingService crawlingService;

    @PostMapping("/send")
    public ResponseEntity<EmailResponseDto> sendNewsletter(
        @AuthenticationPrincipal UserDetails userDetails) {

        try {
            // 1. 세션에서 사용자 정보 조회
            User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            log.info("이메일 발송 요청: userId={}, email={}, name={}",
                user.getId(), user.getEmail(), user.getName());

            // 2. 크롤링된 뉴스와 채용 공고 조회
            List<CrawledNewsDto> newsList = crawlingService.getRecentNews();
            List<CrawledJobsDto> jobsList = crawlingService.getRecentJobs();

            log.info("크롤링 데이터 조회 완료: 뉴스 {}건, 채용 {}건",
                newsList.size(), jobsList.size());

            // 3. 이메일 발송
            EmailResponseDto response = emailService.sendNewsletter(
                user.getId().toString(),  // Long → String 변환
                user.getEmail(),          // String
                user.getName(),           // String (User 엔티티에 있음!)
                newsList,
                jobsList
            );

            log.info("이메일 발송 결과: success={}, recipientEmail={}",
                response.success(), response.recipientEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("이메일 발송 중 오류 발생", e);

            // 에러 응답 반환
            EmailResponseDto errorResponse = new EmailResponseDto(
                false,
                "이메일 발송 중 오류가 발생했습니다: " + e.getMessage(),
                userDetails.getUsername(),
                0,
                0
            );

            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
