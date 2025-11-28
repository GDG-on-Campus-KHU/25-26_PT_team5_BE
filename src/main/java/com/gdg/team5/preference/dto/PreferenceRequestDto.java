package com.gdg.team5.preference.dto;

import java.util.List;

public record PreferenceRequestDto(
    List<Long> preferenceIds
) {
}
