package org.example.expert.domain.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Slf4j
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AccessDeniedException {

        Object role = request.getAttribute("userRole");
        Object userId = request.getAttribute("userId");

        if (role == null || !"ADMIN".equals(role.toString())) {
            log.warn("[AdminCheck] 관리자 권한 없음 - userId={}, role={}, URL={}", userId, role, request.getRequestURI());
            throw new AccessDeniedException("관리자 권한이 없습니다.");
        }

        log.info("[AdminCheck] 관리자 권한 확인 > 접근 허용 - userId={}, role={}, Time={}", userId, role, LocalDateTime.now());

        return true;
    }
}
