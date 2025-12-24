package com.nhnacademy._vidiafront.global.filter;

import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthCookieFilter extends OncePerRequestFilter {
    private final AuthApiClient authApiClient;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //String path = request.getRequestURI();

        Cookie[] cookies = request.getCookies();

        boolean hasSes = false;
        boolean hasAut = false;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("SES".equals(c.getName())) {
                    hasSes = true;
                }
                if ("AUT".equals(c.getName())){
                    hasAut = true;
                }
            }
        }
        if (!hasSes && hasAut) { // SES는 없고 AUT는 있음
            // SES 재발급 로직 호출
            TokenResponse tokenResponse = authApiClient.reissueToken(getRefreshTokenFromCookie(request));
            String newAccessToken = tokenResponse.accessToken();
            String newRefreshUuid = tokenResponse.refreshUuid();
            Cookie accessCookie = new Cookie("SES", newAccessToken);
            accessCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
            accessCookie.setSecure(false);             // HTTPS 환경이면 true
            accessCookie.setPath("/");
            accessCookie.setMaxAge(30 * 60);
            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("AUT", newRefreshUuid);
            refreshCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
            refreshCookie.setSecure(false);             // HTTPS 환경이면 true
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshCookie);
            request.setAttribute("NEW_SES", newAccessToken);
            request.setAttribute("NEW_AUT", newRefreshUuid);
        }

        if (hasSes && !hasAut) {
            deleteCookie("SES", response);
            deleteCookie("AUT", response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);


    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");     // 로그인 때 설정한 path와 반드시 동일
        cookie.setMaxAge(0);     // 즉시 만료
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 로그인 때 false면 여기서도 false

        response.addCookie(cookie);
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
