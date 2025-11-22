package com.gdg.team5.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceRequestDto {
    private List<Long> preferenceIds;
}
