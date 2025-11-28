package com.gdg.team5.mail.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "전송_상태")
    private String status;

    @Column(name = "실패_시_에러_메시지")
    private String errorMessage;

    public void setId(Integer id) {
        this.id = id;
    }
}
