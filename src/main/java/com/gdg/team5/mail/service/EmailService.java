package com.gdg.team5.mail.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.jobposting.repository.JobPostingsRepository;
import com.gdg.team5.mail.domain.EmailLog;
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
    private final JobPostingsRepository jobPostingsRepository;  // ✅ 추가!

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 뉴스레터 발송
     */
    public EmailResponseDto sendNewsletter(String userId, String userEmail, String userName) {
        try {
            // DB에서 최근 뉴스 조회
            List<CrawledNewsDto> newsList = getRecentNewsFromDb();

            // DB에서 최근 채용공고 조회
            List<CrawledJobsDto> jobsList = getRecentJobsFromDb();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(userEmail);
            helper.setSubject(String.format("🚀 [GDG] 오늘의 소식 | 뉴스 %d건, 채용 %d건",
                newsList.size(),
                jobsList.size()));

            String htmlContent = emailTemplateBuilder.buildNewsletterTemplate(userName, newsList, jobsList);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            saveEmailLog(userId, newsList, jobsList, true, null);

            log.info("이메일 발송 완료: {}", userEmail);

            return new EmailResponseDto(
                true,
                "이메일이 성공적으로 발송되었습니다.",
                userEmail,
                newsList.size(),
                jobsList.size()
            );

        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", userEmail, e);
            saveEmailLog(userId, null, null, false, e.getMessage());

            return new EmailResponseDto(
                false,
                "이메일 발송 중 오류가 발생했습니다: " + e.getMessage(),
                userEmail,
                0,
                0
            );
        }
    }

    /**
     * DB에서 최근 뉴스 조회 및 CrawledNewsDto 변환
     */
    private List<CrawledNewsDto> getRecentNewsFromDb() {
        log.info("DB에서 최근 뉴스 조회 시작");

        // 최근 10개 뉴스 조회
        List<News> recentNews = newsRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();

        // News → CrawledNewsDto 변환
        List<CrawledNewsDto> result = recentNews.stream()
            .map(news -> new CrawledNewsDto(
                news.getSource(),
                news.getExternalId(),
                news.getTitle(),
                summarizeContent(news.getContent()),  // 200자 요약
                news.getUrl(),
                news.getPublishedDate(),
                news.getCategory(),
                news.getReporter(),
                news.getProvider(),
                news.getThumbnailUrl()
            ))
            .collect(Collectors.toList());

        log.info("DB에서 뉴스 {}건 조회 완료", result.size());
        return result;
    }

    /**
     * DB에서 최근 채용공고 조회 및 CrawledJobsDto 변환
     */
    private List<CrawledJobsDto> getRecentJobsFromDb() {
        log.info("DB에서 최근 채용공고 조회 시작");

        // 최근 10개 채용공고 조회
        List<JobPostings> recentJobs = jobPostingsRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();

        // JobPostings → CrawledJobsDto 변환
        List<CrawledJobsDto> result = recentJobs.stream()
            .map(job -> new CrawledJobsDto(
                job.getSource(),
                job.getExternalId(),
                job.getTitle(),
                job.getCompanyName(),
                summarizeContent(job.getContent()),
                job.getUrl(),
                job.getPostedDate(),
                job.getDeadLine(),
                job.getCategory(),
                job.getTechStack(),
                job.getLocation(),
                job.getExpLevel(),
                job.getThumbnailUrl()
            ))
            .collect(Collectors.toList());

        log.info("DB에서 채용공고 {}건 조회 완료", result.size());
        return result;
    }

    /**
     * 본문 요약 (200자 제한)
     */
    private String summarizeContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        return content.length() <= 200
            ? content
            : content.substring(0, 200) + "...";
    }

    /**
     * 이메일 발송 로그 저장
     */
    private void saveEmailLog(String userId,
                              List<CrawledNewsDto> newsList,
                              List<CrawledJobsDto> jobsList,
                              boolean isSuccess,
                              String errorMsg) {
        try {
            String newsStr = null;
            if (newsList != null && !newsList.isEmpty()) {
                newsStr = newsList.stream()
                    .map(CrawledNewsDto::title)
                    .limit(5)
                    .collect(Collectors.joining(", "));

                if (newsStr.length() > 255) {
                    newsStr = newsStr.substring(0, 252) + "...";
                }
            }

            String jobsStr = null;
            if (jobsList != null && !jobsList.isEmpty()) {
                jobsStr = jobsList.stream()
                    .map(CrawledJobsDto::title)
                    .limit(5)
                    .collect(Collectors.joining(", "));

                if (jobsStr.length() > 255) {
                    jobsStr = jobsStr.substring(0, 252) + "...";
                }
            }

            EmailLog log = EmailLog.builder()
                .userId(userId)
                .sentDate(isSuccess ? LocalDateTime.now() : null)
                .news(newsStr)
                .job(jobsStr)
                .status(isSuccess ? "SUCCESS" : "FAIL")
                .errorMessage(errorMsg)
                .build();

            emailLogRepository.save(log);

        } catch (Exception e) {
            log.error("이메일 로그 저장 실패", e);
        }
    }
}
