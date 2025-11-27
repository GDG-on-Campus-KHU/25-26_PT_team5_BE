package com.gdg.team5.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutResponse {

    private boolean success;
    private String message;
}