package com.gdg.team5.preference.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.common.exception.BaseException;
import com.gdg.team5.common.response.BaseResponseStatus;
import com.gdg.team5.preference.domain.Preference;
import com.gdg.team5.preference.domain.UserPreference;
import com.gdg.team5.preference.dto.PreferenceRequestDto;
import com.gdg.team5.preference.repository.PreferenceRepository;
import com.gdg.team5.preference.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final PreferenceRepository preferenceRepository;

    @Transactional
    public void savePreference(PreferenceRequestDto requestDto) {
        // 임시 유저 객체
        User user = User.builder()
            .id(1L)
            .build();

        List<Long> preferenceIds = requestDto.getPreferenceIds();
        for (Long preferenceId : preferenceIds) {
            Preference preference = preferenceRepository.findById(preferenceId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PREFERENCE_NOT_FOUND));
            UserPreference userPreference = UserPreference.builder()
                .user(user)
                .preference(preference)
                .build();
        }
    }
}
