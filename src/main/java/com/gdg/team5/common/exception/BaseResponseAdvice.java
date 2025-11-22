package com.gdg.team5.common.exception;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.common.response.BaseResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseResponseAdvice {

    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseResponseStatus> handleBaseException(BaseException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
