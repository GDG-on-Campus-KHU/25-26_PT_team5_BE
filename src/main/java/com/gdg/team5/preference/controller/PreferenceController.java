package com.gdg.team5.preference.controller;

import com.gdg.team5.auth.domain.CustomUserDetails;
import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.preference.dto.PreferenceRequestDto;
import com.gdg.team5.preference.dto.PreferenceResponseDto;
import com.gdg.team5.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preference")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PatchMapping("")
    public BaseResponse<String> updatePreference(@RequestBody PreferenceRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        preferenceService.updatePreference(requestDto, userDetails.getId());
        return new BaseResponse<>("관심 분야 저장 성공");
    }

    @GetMapping("")
    public BaseResponse<List<PreferenceResponseDto>> getPreference(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(preferenceService.getPreference(userDetails.getId()));
    }

    @GetMapping("/all")
    public BaseResponse<List<PreferenceResponseDto>> getAllPreference() {
        return new BaseResponse<>(preferenceService.getAllPreference());
    }
}
