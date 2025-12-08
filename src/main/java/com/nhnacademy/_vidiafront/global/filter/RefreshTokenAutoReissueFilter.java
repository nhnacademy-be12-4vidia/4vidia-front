package com.nhnacademy._vidiafront.global.filter;

import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RefreshTokenAutoReissueFilter extends OncePerRequestFilter {

    private final RestClient restClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;

        String refreshToken = getRefreshToken(request);

        // ✅ access 없고, refresh 존재할 때만 재발급
        if (accessToken == null && refreshToken != null) {
            try {
                TokenResponse tokenResponse = restClient.post()
                        .uri("/api/v1/auth/auth/reissue")
                        .cookies(cookies -> cookies.add("refresh", refreshToken))
                        .retrieve()
                        .body(TokenResponse.class);

                if (tokenResponse != null) {
                    request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());

                    Cookie cookie = new Cookie("refresh", tokenResponse.refreshToken());
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                
            } catch (Exception ignored) {
                // 조용히 실패 처리 (로그인 상태만 유지 안 됨)
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        return uri.startsWith("/css")
                || uri.startsWith("/js")
                || uri.startsWith("/images")
                || uri.startsWith("/vendor")
                || uri.startsWith("/favicon.ico");
    }
}
