package com.gdg.team5.preference.controller;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.preference.domain.Preference;
import com.gdg.team5.preference.dto.PreferenceRequestDto;
import com.gdg.team5.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preference")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PatchMapping("")
    public BaseResponse<String> updatePreference(@RequestBody PreferenceRequestDto requestDto) {
        preferenceService.updatePreference(requestDto);
        return new BaseResponse<>("관심 분야 저장 성공");
    }

    @GetMapping("")
    public BaseResponse<List<Preference>> getPreference() {
        return new BaseResponse<>(preferenceService.getPreference());
    }

    @GetMapping("/all")
    public BaseResponse<List<Preference>> getAllPreference() {
        return new BaseResponse<>(preferenceService.getAllPreference());
    }
}
