package com.gdg.team5.mail.service;

import com.gdg.team5.crawling.dto.CrawledJobsDto;
import com.gdg.team5.crawling.dto.CrawledNewsDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailTemplateBuilder {

    public String buildNewsletterTemplate(String userName,
                                          List<CrawledNewsDto> newsList,
                                          List<CrawledJobsDto> jobsList) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
        html.append(".container { max-width: 800px; margin: 0 auto; background-color: white; }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 20px; text-align: center; }");
        html.append(".header h1 { color: white; margin: 0; font-size: 32px; }");
        html.append(".content { padding: 30px; }");
        html.append(".section-title { font-size: 24px; color: #333; margin: 30px 0 20px 0; padding-bottom: 10px; border-bottom: 3px solid #667eea; }");
        html.append(".card { border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; margin-bottom: 20px; transition: box-shadow 0.3s; }");
        html.append(".card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }");
        html.append(".card-title { font-size: 18px; font-weight: bold; color: #333; margin-bottom: 10px; }");
        html.append(".card-company { font-size: 16px; color: #667eea; margin-bottom: 10px; }");
        html.append(".card-content { font-size: 14px; color: #555; line-height: 1.6; margin-bottom: 15px; }");
        html.append(".card-meta { font-size: 13px; color: #999; margin-bottom: 10px; }");
        html.append(".card-thumbnail { width: 100%; max-height: 200px; object-fit: cover; border-radius: 6px; margin-bottom: 15px; }");
        html.append(".btn { display: inline-block; padding: 10px 20px; background-color: #667eea; color: white; text-decoration: none; border-radius: 5px; font-size: 14px; }");
        html.append(".btn:hover { background-color: #5568d3; }");
        html.append(".tag { display: inline-block; padding: 4px 8px; background-color: #f0f0f0; color: #666; border-radius: 3px; font-size: 12px; margin-right: 5px; }");
        html.append(".footer { background-color: #f9f9f9; padding: 20px; text-align: center; color: #999; font-size: 12px; }");
        html.append("</style>");
        html.append("</head><body>");

        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<h1>오늘의 소식</h1>");
        html.append("<p style='color: white; margin: 10px 0 0 0;'>안녕하세요, ").append(userName).append("님!</p>");
        html.append("</div>");

        html.append("<div class='content'>");

        // 뉴스 섹션
        if (newsList != null && !newsList.isEmpty()) {
            html.append("<h2 class='section-title'>📰 최신 뉴스 (").append(newsList.size()).append(")</h2>");

            for (CrawledNewsDto news : newsList) {
                html.append("<div class='card'>");

                // 썸네일
                if (news.thumbnailUrl() != null && !news.thumbnailUrl().isEmpty()) {
                    html.append("<img src='").append(news.thumbnailUrl()).append("' class='card-thumbnail' alt='뉴스 썸네일'>");
                }

                // 제목
                html.append("<div class='card-title'>").append(news.title()).append("</div>");

                // 메타 정보 (reporter, provider - CrawledNewsDto의 추가 정보!)
                if (news.reporter() != null || news.provider() != null) {
                    html.append("<div class='card-meta'>");
                    if (news.reporter() != null) {
                        html.append("✍️ ").append(news.reporter());
                    }
                    if (news.provider() != null) {
                        if (news.reporter() != null) html.append(" | ");
                        html.append("📡 ").append(news.provider());
                    }
                    html.append("</div>");
                }

                // 본문
                if (news.content() != null && !news.content().isEmpty()) {
                    html.append("<div class='card-content'>").append(news.content()).append("</div>");
                }

                // 카테고리
                if (news.category() != null) {
                    html.append("<span class='tag'>").append(news.category()).append("</span>");
                }

                html.append("<br><br>");
                html.append("<a href='").append(news.url()).append("' class='btn'>기사 전문 보기 →</a>");
                html.append("</div>");
            }
        }

        // 채용 공고 섹션
        if (jobsList != null && !jobsList.isEmpty()) {
            html.append("<h2 class='section-title'>💼 채용 공고 (").append(jobsList.size()).append(")</h2>");

            for (CrawledJobsDto job : jobsList) {
                html.append("<div class='card'>");

                // 썸네일
                if (job.thumbnailUrl() != null && !job.thumbnailUrl().isEmpty()) {
                    html.append("<img src='").append(job.thumbnailUrl()).append("' class='card-thumbnail' alt='회사 로고'>");
                }

                // 제목
                html.append("<div class='card-title'>").append(job.title()).append("</div>");

                // 회사명
                html.append("<div class='card-company'>🏢 ").append(job.companyName()).append("</div>");

                // 본문
                if (job.content() != null && !job.content().isEmpty()) {
                    html.append("<div class='card-content'>").append(job.content()).append("</div>");
                }

                // 태그들 (CrawledJobsDto의 추가 정보!)
                boolean hasTags = false;
                if (job.category() != null) {
                    html.append("<span class='tag'>").append(job.category()).append("</span>");
                    hasTags = true;
                }
                if (job.expLevel() != null) {
                    html.append("<span class='tag'>").append(job.expLevel()).append("</span>");
                    hasTags = true;
                }
                if (job.location() != null) {
                    html.append("<span class='tag'>📍 ").append(job.location()).append("</span>");
                    hasTags = true;
                }

                if (hasTags) {
                    html.append("<br><br>");
                }

                // 기술 스택
                if (job.techStack() != null && !job.techStack().isEmpty()) {
                    html.append("<div style='margin: 10px 0;'><strong>기술 스택:</strong> ");
                    String[] techs = job.techStack().split(",");
                    for (String tech : techs) {
                        html.append("<span class='tag'>").append(tech.trim()).append("</span>");
                    }
                    html.append("</div>");
                }

                // 마감일
                if (job.deadLine() != null) {
                    html.append("<div style='font-size: 13px; color: #e74c3c; margin: 10px 0;'>");
                    html.append("⏰ 마감일: ").append(job.deadLine());
                    html.append("</div>");
                }

                html.append("<a href='").append(job.url()).append("' class='btn'>지원하기 →</a>");
                html.append("</div>");
            }
        }

        html.append("</div>");

        html.append("<div class='footer'>");
        html.append("<p>이 메일은 GDG on Campus KHU Team 5에서 발송되었습니다.</p>");
        html.append("</div>");

        html.append("</div></body></html>");

        return html.toString();
    }
}
