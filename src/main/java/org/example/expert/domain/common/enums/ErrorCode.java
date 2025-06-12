package org.example.expert.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러코드와 에러메세지 관리
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {


    // 인가 관련
    REQUIRE_ADMIN(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");

    private final HttpStatus status;
    private final String message;

}
