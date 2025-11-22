package com.gdg.team5.jobposting.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;
    private String title;
    private String companyName;
    @Lob
    private String content;
    private String url;
    private String postedDate;
    private String deadLine;
    private String category;
    private String tech_stack;
    private String location;
    private String exp_level;
    private String thumbnailUrl;
}
