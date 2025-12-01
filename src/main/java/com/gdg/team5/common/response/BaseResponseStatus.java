package com.gdg.team5.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    // 성공
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    // 기타 에러
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    USER_NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "해당 유저를 찾을 수 없습니다."), // 💡 예시로 2001 코드를 부여하고 HTTP 404 지정
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다.");
    
    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
