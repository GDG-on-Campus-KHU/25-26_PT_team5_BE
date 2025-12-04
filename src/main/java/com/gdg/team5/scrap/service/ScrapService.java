package com.gdg.team5.scrap.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus;
import com.gdg.team5.jobposting.domain.JobPostings;
import com.gdg.team5.jobposting.repository.JobPostingsRepository;
import com.gdg.team5.news.domain.News;
import com.gdg.team5.news.repository.NewsRepository;
import com.gdg.team5.scrap.domain.Scrap;
import com.gdg.team5.scrap.domain.ScrapType;
import com.gdg.team5.scrap.dto.ScrapJobResponseDto;
import com.gdg.team5.scrap.dto.ScrapNewsResponseDto;
import com.gdg.team5.scrap.dto.ScrapResponseDto;
import com.gdg.team5.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final JobPostingsRepository jobPostingsRepository;

    // 스크랩 저장
    @Transactional
    public void saveScrap(Long userId, ScrapType type, Long contentId) {
        // 이미 존재하는 스크랩인 경우 무시
        if (scrapRepository.existsByUserIdAndTypeAndContentId(userId, type, contentId))
            return;
        // 저장
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Scrap scrap = Scrap.builder()
                .user(user)
                .type(type)
                .contentId(contentId)
                .build();
        scrapRepository.save(scrap);
    }

    // 스크랩 목록 조회
    public List<ScrapResponseDto> getScraps(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        // 모든 스크랩 내역 조회
        List<Scrap> scraps = scrapRepository.findAllByUserId(user.getId());
        // 없으면 빈 리스트 반환
        if (scraps.isEmpty()) {
            return List.of();
        }
        // 뉴스/공고 아이디 분리
        List<Long> newsIds = new ArrayList<>();
        List<Long> jobIds = new ArrayList<>();
        for (Scrap scrap : scraps) {
            if (scrap.getType() == ScrapType.JOB) {
                jobIds.add(scrap.getContentId());
            } else {
                newsIds.add(scrap.getContentId());
            }
        }
        // 각각 {뉴스 ID : 뉴스 객체} 형태의 Map으로 매핑
        // O(1)에 조회 가능
        Map<Long, News> newsMap = newsIds.isEmpty() ? Collections.emptyMap() :
                newsRepository.findAllById(newsIds).stream()
                        .collect(Collectors.toMap(News::getId, Function.identity()));
        Map<Long, JobPostings> jobMap = jobIds.isEmpty() ? Collections.emptyMap() :
                jobPostingsRepository.findAllById(jobIds).stream()
                        .collect(Collectors.toMap(JobPostings::getId, Function.identity()));
        // DTO로 변환하여 반환
        return scraps.stream()
                .map(scrap -> {
                    if (scrap.getType() == ScrapType.JOB) {
                        return ScrapJobResponseDto.from(scrap, jobMap.get(scrap.getContentId()));
                    } else {
                        return ScrapNewsResponseDto.from(scrap, newsMap.get(scrap.getContentId()));
                    }
                })
                .collect(Collectors.toList());
    }

    // 스크랩 삭제
    @Transactional
    public void deleteScrap(Long scrapId) {
        scrapRepository.deleteById(scrapId);
    }
}
