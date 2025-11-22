package com.gdg.team5.preference.service;

import com.gdg.team5.auth.domain.User;
import com.gdg.team5.auth.repository.UserRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

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
            userPreferenceRepository.save(userPreference);
        }
    }

    @Transactional
    public void updatePreference(Long userId, PreferenceRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(BaseResponseStatus.PREFERENCE_NOT_FOUND));
        userPreferenceRepository.deleteByUserId(userId);
        List<Long> preferenceIds = requestDto.getPreferenceIds();
        for (Long preferenceId : preferenceIds) {
            Preference preference = preferenceRepository.findById(preferenceId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PREFERENCE_NOT_FOUND));
            UserPreference userPreference = UserPreference.builder()
                .user(user)
                .preference(preference)
                .build();
            userPreferenceRepository.save(userPreference);
        }
    }

    public List<Preference> getPreference(Long userId) {
        List<UserPreference> userPreferences = userPreferenceRepository.findByUserId(userId);
        if (userPreferences.isEmpty()) {
            throw new BaseException(BaseResponseStatus.PREFERENCE_USER_NOT_FOUND);
        }
        return userPreferences.stream()
            .map(UserPreference::getPreference)
            .collect(Collectors.toList());
    }
}
