package com.gdg.team5.common.exception;
import com.gdg.team5.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final BaseResponseStatus status;

    public CustomException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}