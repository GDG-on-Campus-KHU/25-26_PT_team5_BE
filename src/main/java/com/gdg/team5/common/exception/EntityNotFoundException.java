package com.gdg.team5.common.exception;

import com.gdg.team5.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final BaseResponseStatus status;

    public EntityNotFoundException(BaseResponseStatus status) {
        // 부모에게 BaseResponseStatus에 정의된 메시지를 전달
        super(status.getMessage()); 
        this.status = status;
    }
}