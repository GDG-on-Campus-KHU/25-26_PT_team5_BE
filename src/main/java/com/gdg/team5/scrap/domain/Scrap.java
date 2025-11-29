package com.gdg.team5.scrap.domain;

import com.gdg.team5.auth.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScrapType type;

    @Column(nullable = false)
    private Long contentId;

    @Builder
    public Scrap(User user, ScrapType type, Long contentId) {
        this.user = user;
        this.type = type;
        this.contentId = contentId;
    }
}
