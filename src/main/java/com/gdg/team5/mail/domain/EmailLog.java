package com.gdg.team5.mail.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_log")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "이메일", nullable = false, unique = true)
    private Integer id;

    @Column(name = "유저_아이디", nullable = false)
    private String userId;

    @Column(name = "보낸_날짜")
    private LocalDateTime sentDate;

    @Column(name = "뉴스")
    private String news;

    @Column(name = "직업")
    private String job;

    @Enumerated(EnumType.STRING)
    @Column(name = "전송_상태")
    private EmailStatus status;

    @Column(name = "실패_시_에러_메시지")
    private String errorMessage;

    @Builder
    public EmailLog(String userId, LocalDateTime sentDate, String news,
                    String job, EmailStatus status, String errorMessage) {
        this.userId = userId;
        this.sentDate = sentDate;
        this.news = news;
        this.job = job;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
