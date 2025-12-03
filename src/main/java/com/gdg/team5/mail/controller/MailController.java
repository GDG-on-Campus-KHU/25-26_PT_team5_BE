package com.gdg.team5.mail.controller;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.repository.UserRepository;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping("/send")
    public ResponseEntity<EmailResponseDto> sendNewsletter(
        @AuthenticationPrincipal UserDetails userDetails) {

        try {
            // 1. 세션에서 사용자 정보 조회
            User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            log.info("이메일 발송 요청: userId={}, email={}, name={}",
                user.getId(), user.getEmail(), user.getName());

            // 2. 이메일 발송 (DB에서 자동으로 조회)
            EmailResponseDto response = emailService.sendNewsletter(
                user.getId().toString(),
                user.getEmail(),
                user.getName()
            );

            log.info("이메일 발송 결과: success={}, recipientEmail={}",
                response.success(), response.recipientEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("이메일 발송 중 오류 발생", e);

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
