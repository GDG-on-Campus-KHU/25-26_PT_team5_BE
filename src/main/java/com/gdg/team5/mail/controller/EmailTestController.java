package com.gdg.team5.mail.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @GetMapping("/test")
    public String testEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(fromEmail);  // 자기 자신에게 발송
            helper.setSubject("Gmail SMTP 테스트");
            helper.setText(
                "<h1 style='color: #667eea;'>테스트 성공!</h1>" +
                    "<p>Gmail SMTP로 이메일이 정상적으로 발송됩니다.</p>" +
                    "<p><strong>GDG on Campus KHU</strong></p>",
                true
            );

            mailSender.send(message);

            return "이메일 발송 성공! 받은편지함을 확인하세요.";

        } catch (MessagingException e) {
            return "이메일 발송 실패: " + e.getMessage();
        }
    }
}
