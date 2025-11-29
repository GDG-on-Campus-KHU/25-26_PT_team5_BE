package com.gdg.team5.scrap.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Type에 따라 NEWS/JOB DTO 분리
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ScrapNewsResponseDto.class, name = "NEWS"),
    @JsonSubTypes.Type(value = ScrapJobResponseDto.class, name = "JOB")
})
public interface ScrapResponseDto {
    // 공통 필드 Get 메서드
    Long id();

    String source();

    String externalId();

    String title();

    String content();

    String url();

    String category();

    String thumbnailUrl();
}
