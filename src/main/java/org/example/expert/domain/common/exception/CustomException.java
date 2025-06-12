package org.example.expert.domain.common.exception;

import lombok.Getter;
import org.example.expert.domain.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    //에러코드와 에러메세지 커스텀으로 만들어서 예외처리
    private final HttpStatus status;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }
}
