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

    @Transactional
    public void saveScrap(Long userId, ScrapType type, Long contentId) {
        if (scrapRepository.existsByUserIdAndTypeAndContentId(userId, type, contentId))
            return;
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Scrap scrap = Scrap.builder()
            .user(user)
            .type(type)
            .contentId(contentId)
            .build();
        scrapRepository.save(scrap);
    }

    public List<ScrapResponseDto> getScraps() {
        // 임시 유저 객체
        User user = User.builder()
            .id(1L)
            .build();

        List<Scrap> scraps = scrapRepository.findAllByUserId(user.getId());
        if (scraps.isEmpty()) {
            return List.of();
        }
        List<Long> newsIds = new ArrayList<>();
        List<Long> jobIds = new ArrayList<>();
        for (Scrap scrap : scraps) {
            if (scrap.getType() == ScrapType.JOB) {
                jobIds.add(scrap.getContentId());
            } else {
                newsIds.add(scrap.getContentId());
            }
        }

        Map<Long, News> newsMap = newsIds.isEmpty() ? Collections.emptyMap() :
            newsRepository.findAllById(newsIds).stream()
                .collect(Collectors.toMap(News::getId, Function.identity()));
        Map<Long, JobPostings> jobMap = jobIds.isEmpty() ? Collections.emptyMap() :
            jobPostingsRepository.findAllById(jobIds).stream()
                .collect(Collectors.toMap(JobPostings::getId, Function.identity()));

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

    @Transactional
    public void deleteScrap(Long scrapId) {
        scrapRepository.deleteById(scrapId);
    }
}
