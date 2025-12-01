package com.gdg.team5.common.advice;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.common.response.BaseResponseStatus;
import com.gdg.team5.common.exception.CustomException; // 3.1에서 정의한 예외
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 모든 Controller의 예외를 처리
public class BaseResponseAdvice {

    // 1. 커스텀 예외 처리 (비즈니스 로직 오류: 예. 이미 존재하는 이메일)
    // 서비스 계층에서 CustomException을 던지면 여기서 잡습니다.
    @ExceptionHandler(CustomException.class)
    public BaseResponse<?> handleCustomException(CustomException e) {
        log.error("Custom Exception: {}", e.getMessage());
        // CustomException 내부의 BaseResponseStatus를 사용하여 응답을 생성
        return new BaseResponse<>(e.getStatus());
    }

    // 2. 인증/인증 관련 예외 처리 (Controller의 IllegalArgumentException 대체)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // HTTP 401 상태 코드를 명시적으로 설정
    public BaseResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        return new BaseResponse<>(BaseResponseStatus.UNAUTHORIZED_ERROR);
    }
    
    // 3. 가장 포괄적인 서버 예외 처리 (Controller의 Exception 대체)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500 상태 코드를 명시적으로 설정
    public BaseResponse<?> handleAllException(Exception e) {
        log.error("Server Error: {}", e.getMessage());
        return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
    }
}