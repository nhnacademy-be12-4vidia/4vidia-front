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
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;

        String refreshToken = getRefreshToken(request);
        boolean isAccessExpired = accessToken != null && jwtUtil.isTokenExpired(accessToken);

        // ✅ access 없고, refresh 존재할 때만 재발급
        if ((accessToken == null || isAccessExpired) && refreshToken != null) {
            try {
                TokenResponse tokenResponse = restClient.post()
                        .uri("/api/v1/auth/auth/reissue")
                        .cookies(cookies -> cookies.add("refresh", refreshToken))
                        .retrieve()
                        .body(TokenResponse.class);

                if (tokenResponse != null) {
                    request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());

                    Cookie cookie = new Cookie("refresh", tokenResponse.refreshToken());
                    cookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
                    cookie.setSecure(false);             // HTTPS 환경이면 true
                    cookie.setPath("/");
                    cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
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
