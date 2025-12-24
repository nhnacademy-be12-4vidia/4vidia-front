package com.nhnacademy._vidiafront.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class LoginStatus {
    private final HttpServletRequest request;

    /**
     * 로그인 여부
     */
    public boolean isLoggedIn() {

        return getRefreshTokenFromCookie(request)!= null? true: false;
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("AUT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
