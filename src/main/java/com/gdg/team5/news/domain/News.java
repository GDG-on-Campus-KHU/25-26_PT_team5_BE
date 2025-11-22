package com.gdg.team5.news.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;
    private String title;
    @Lob
    private String content;
    private String url;
    private String publishedDate;
    private String category;
    private String reporter;
    private String provider;
    private String thumbnailUrl;


}
