package com.nhnacademy._vidiafront.global.controller;

import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.global.filter.JwtUtil;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserApiClient userApiClient;
    private static final String COMPLETE_PROFILE_PATH = "/complete-profile";
    private final JwtUtil jwtUtil;

    /**
     * layout.html에서 사용 (ex 마이페이지 누르면 열리는 목록 고정시키기?)
     * */
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        // 모든 템플릿에서 ${currentUri}로 접근 가능
        return request.getRequestURI(); 
    }
    private HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpServletRequest request) throws IOException {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;

        String refreshToken = getRefreshToken(request);

        boolean isLoggedIn = false;

        if (accessToken != null) {
            isLoggedIn = true;
        } else if (refreshToken != null) {
            // access가 아직 재발급되기 전이어도 로그인 UI 유지
            isLoggedIn = true;
        }

        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            try {
                model.addAttribute("userName", userApiClient.getUserName());
            } catch (Exception e) {
                model.addAttribute("userName", "회원");
            }
        } else {
            model.addAttribute("userName", "비회원");
        }
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


}