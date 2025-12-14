package com.nhnacademy._vidiafront.global.interceptor;

import com.nhnacademy._vidiafront.global.filter.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class TempUserInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {


        String uri = request.getRequestURI();

        if (
                uri.startsWith("/complete-profile")
                        || uri.startsWith("/auth/logout")
                        || uri.startsWith("/auth/check-email")
        ) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) return true;

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) return true;

        try {
            String status = jwtUtil.getStatus(accessToken);

            if ("TEMP".equals(status)) {
                response.sendRedirect("/complete-profile");
                return false; // 🔥 요청 중단
            }
        } catch (JwtException e) {
            return true;
        }

        return true;
    }
}
