package org.example.expert.domain.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect // AOP 담당 클래스를 나타내는 어노테이션
@Component
@RequiredArgsConstructor
public class AdminLoggingAspect {

    private final HttpServletRequest request;

    // Java 객체를 JSON 문자열로 변환
    private final ObjectMapper objectMapper;

    /**
     * 관리자 요청 시 해당 메서드 실행 전/후에 끼어들어서 요청/응답 정보를 JSON 형식으로 로깅하는 AOP
     * @param joinPoint 실제 호출될 메서드 정보
     * @return 메서드 실행 결과를 JSON 으로 Logging 후 반환
     * @throws Throwable 예외 발생 시 호출한 코드로 던짐
     */

    @Around("execution(* org.example.expert.*.*.*.CommentAdminController.*(..)) || " +
            "execution(* org.example.expert.*.*.*.UserAdminController.*(..))")
    public Object logAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object userId = request.getAttribute("userId");
        String uri = request.getRequestURI();
        LocalDateTime currentTime = LocalDateTime.now();

        Object requestBody = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> !(arg instanceof Long))
                .findFirst()
                .orElse(null);

        String requestJson = requestBody != null ? objectMapper.writeValueAsString(requestBody) : "requestBody 없음";
        log.info("[AdminRequest] userId={}, Time={}, URI={}, requestBody={}", userId, currentTime, uri, requestJson);

        Object result = joinPoint.proceed();

        String responseJson = result != null ? objectMapper.writeValueAsString(result) : "responseBody 없음";
        log.info("[AdminResponse] userId={}, Time={}, URI={}, requestBody={}", userId, currentTime, uri, responseJson);

        return result;
    }


}
