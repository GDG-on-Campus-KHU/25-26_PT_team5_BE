package com.gdg.team5.mail.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.jobposting.repository.JobPostingsRepository;
import com.gdg.team5.mail.domain.EmailLog;
import com.gdg.team5.mail.domain.EmailStatus;
import com.gdg.team5.mail.dto.EmailResponseDto;
import com.gdg.team5.mail.repository.EmailLogRepository;
import com.gdg.team5.news.domain.News;
import com.gdg.team5.news.repository.NewsRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailLogRepository emailLogRepository;
    private final NewsRepository newsRepository;
    private final JobPostingsRepository jobPostingsRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * [테스트용 수정] 전체 발송인 척 하지만, 사실 내 메일로 한 통만 보냄
     */
    public void sendNewsletterToAllUsers() {
        log.info("📢 [테스트 모드] 뉴스레터 발송 시작");

        String myEmail = "dhwlsgur795@khu.ac.kr";
        String myName = "테스터";
        String myId = "test-admin";

        // 딱 한 번만 실행 (루프 없음)
        sendNewsletter(myId, myEmail, myName);

        log.info("📢 [테스트 모드] 발송 완료");
    }


    /**
     * (기존 메서드) 개별 뉴스레터 발송
     */
    public EmailResponseDto sendNewsletter(String userId, String userEmail, String userName) {
        // ... (기존 코드 그대로 유지) ...
        try {
            List<CrawledNewsDto> newsList = getRecentNewsFromDb();
            List<CrawledJobsDto> jobsList = getRecentJobsFromDb();

            if (newsList.isEmpty() && jobsList.isEmpty()) {
                log.info("보낼 데이터 없음. User: {}", userEmail);
                return new EmailResponseDto(false, "데이터 없음", userEmail, 0, 0);
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(userEmail);
            helper.setSubject(String.format("🚀 [GDG] 오늘의 소식 | 뉴스 %d건, 채용 %d건",
                newsList.size(), jobsList.size()));

            String htmlContent = emailTemplateBuilder.buildNewsletterTemplate(userId, userName, newsList, jobsList);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            saveEmailLog(userId, newsList, jobsList, true, null);
            log.info("이메일 발송 완료: {}", userEmail);

            return new EmailResponseDto(true, "성공", userEmail, newsList.size(), jobsList.size());

        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", userEmail, e);
            saveEmailLog(userId, null, null, false, e.getMessage());
            return new EmailResponseDto(false, e.getMessage(), userEmail, 0, 0);
        }
    }

    // ... (아래 private 메서드들은 아까 보내드린 것과 동일하게 유지) ...
    private List<CrawledNewsDto> getRecentNewsFromDb() {
        // (기존 코드 유지)
        List<News> recentNews = newsRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
        return recentNews.stream().map(news -> new CrawledNewsDto(
            news.getSource(), news.getExternalId(), news.getTitle(),
            summarizeContent(news.getContent()), news.getUrl(),
            news.getPublishedDate(), news.getCategory(),
            news.getReporter(), news.getProvider(), news.getThumbnailUrl()
        )).collect(Collectors.toList());
    }

    private List<CrawledJobsDto> getRecentJobsFromDb() {
        // (기존 코드 유지)
        List<JobPostings> recentJobs = jobPostingsRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
        return recentJobs.stream().map(job -> new CrawledJobsDto(
            job.getSource(), job.getExternalId(), job.getTitle(),
            job.getCompanyName(), summarizeContent(job.getContent()),
            job.getUrl(), job.getPostedDate(), job.getDeadLine(),
            job.getCategory(), job.getTechStack(), job.getLocation(),
            job.getExpLevel(), job.getThumbnailUrl()
        )).collect(Collectors.toList());
    }

    private String summarizeContent(String content) {
        if (content == null || content.isEmpty()) return "";
        return content.length() <= 200 ? content : content.substring(0, 200) + "...";
    }

    private void saveEmailLog(String userId, List<CrawledNewsDto> newsList, List<CrawledJobsDto> jobsList, boolean isSuccess, String errorMsg) {
        // (기존 코드 유지)
        try {
            String newsStr = null;
            if (newsList != null && !newsList.isEmpty()) {
                newsStr = newsList.stream().map(CrawledNewsDto::title).limit(5).collect(Collectors.joining(", "));
                if (newsStr.length() > 255) newsStr = newsStr.substring(0, 252) + "...";
            }
            String jobsStr = null;
            if (jobsList != null && !jobsList.isEmpty()) {
                jobsStr = jobsList.stream().map(CrawledJobsDto::title).limit(5).collect(Collectors.joining(", "));
                if (jobsStr.length() > 255) jobsStr = jobsStr.substring(0, 252) + "...";
            }
            EmailLog log = EmailLog.builder()
                .userId(userId)
                .sentDate(isSuccess ? LocalDateTime.now() : null)
                .news(newsStr)
                .job(jobsStr)
                .status(isSuccess ? EmailStatus.SUCCESS : EmailStatus.FAIL)
                .errorMessage(errorMsg)
                .build();
            emailLogRepository.save(log);
        } catch (Exception e) {
            log.error("이메일 로그 저장 실패", e);
        }
    }
}
