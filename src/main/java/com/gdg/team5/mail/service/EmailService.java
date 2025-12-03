package com.gdg.team5.mail.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import com.gdg.team5.mail.domain.EmailLog;
import com.gdg.team5.mail.dto.EmailResponseDto;
import com.gdg.team5.mail.dto.JobEmailDto;
import com.gdg.team5.mail.dto.NewsEmailDto;
import com.gdg.team5.mail.repository.EmailLogRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailResponseDto sendNewsletter(String userId, String userEmail, String userName,
                                           List<CrawledNewsDto> crawledNewsList,
                                           List<CrawledJobsDto> crawledJobsList) {
        try {
            List<NewsEmailDto> newsList = convertToNewsEmailDto(crawledNewsList);
            List<JobEmailDto> jobsList = convertToJobEmailDto(crawledJobsList);

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
                "뉴스레터가 성공적으로 발송되었습니다.",
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

    private List<NewsEmailDto> convertToNewsEmailDto(List<CrawledNewsDto> crawledNewsList) {
        if (crawledNewsList == null) {
            return List.of();
        }

        return crawledNewsList.stream()
            .map(news -> new NewsEmailDto(
                news.getTitle(),
                summarizeContent(news.getContent()),
                news.getUrl(),
                news.getThumbnailUrl(),
                news.getCategory(),
                news.getPublishedDate()
            ))
            .collect(Collectors.toList());
    }

    private List<JobEmailDto> convertToJobEmailDto(List<CrawledJobsDto> crawledJobsList) {
        if (crawledJobsList == null) {
            return List.of();
        }

        return crawledJobsList.stream()
            .map(job -> new JobEmailDto(
                job.getTitle(),
                job.getCompanyName(),
                summarizeContent(job.getContent()),
                job.getUrl(),
                job.getThumbnailUrl(),
                job.getLocation(),
                job.getDeadLine()
            ))
            .collect(Collectors.toList());
    }

    private String summarizeContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        if (content.length() <= 200) {
            return content;
        }

        return content.substring(0, 200) + "...";
    }

    private void saveEmailLog(String userId,
                              List<NewsEmailDto> newsList,
                              List<JobEmailDto> jobsList,
                              boolean isSuccess,
                              String errorMsg) {
        try {
            String newsStr = null;
            if (newsList != null && !newsList.isEmpty()) {
                newsStr = newsList.stream()
                    .map(NewsEmailDto::title)
                    .limit(5)
                    .collect(Collectors.joining(", "));

                if (newsStr.length() > 255) {
                    newsStr = newsStr.substring(0, 252) + "...";
                }
            }

            String jobsStr = null;
            if (jobsList != null && !jobsList.isEmpty()) {
                jobsStr = jobsList.stream()
                    .map(JobEmailDto::title)
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
