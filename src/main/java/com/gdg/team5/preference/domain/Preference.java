package com.gdg.team5.preference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private String type;

    @Builder
    public Preference(String type, String keyword) {
        this.type = type;
        this.keyword = keyword;
    }
}
