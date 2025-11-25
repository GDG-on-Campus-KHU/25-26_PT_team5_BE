package com.gdg.team5.scrap.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.repository.UserRepository;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus;
import com.gdg.team5.scrap.domain.Scrap;
import com.gdg.team5.scrap.domain.ScrapType;
import com.gdg.team5.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

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
}
