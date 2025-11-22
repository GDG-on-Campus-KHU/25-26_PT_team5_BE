package com.gdg.team5.preference.controller;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.preference.dto.PreferenceRequestDto;
import com.gdg.team5.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preference")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PostMapping("")
    public BaseResponse<String> savePreference(@RequestBody PreferenceRequestDto requestDto) {
        preferenceService.savePreference(requestDto);
        return new BaseResponse<>("관심 분야 저장 성공");
    }
}
