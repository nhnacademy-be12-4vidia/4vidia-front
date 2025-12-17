package com.nhnacademy._vidiafront.global.auth;

import com.nhnacademy._vidiafront.global.filter.JwtUtil;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class LoginStatus {
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    /**
     * 세션에서 accessToken 가져오기
     */
    public String getAccessToken() {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute("accessToken") : null;
    }

    /**
     * refreshToken 쿠키에서 가져오기
     */
    public String getRefreshToken() {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 로그인 여부
     */
    public boolean isLoggedIn() {
        return getAccessToken() != null || getRefreshToken() != null;
    }

    /**
     * 사용자 이름 조회
     */
    public String getUserName(UserApiClient userApiClient) {
        if (!isLoggedIn()) return "비회원";
        try {
            return userApiClient.getUserName();
        } catch (Exception e) {
            return "회원";
        }
    }
}
