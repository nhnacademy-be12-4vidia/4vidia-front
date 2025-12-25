package com.nhnacademy._vidiafront.global.controller;

import com.nhnacademy._vidiafront.global.auth.LoginStatus;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final LoginStatus loginStatus;
    private static final String COMPLETE_PROFILE_PATH = "/complete-profile";

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
        if (request.getRequestURI().startsWith("/auth/login")|| request.getRequestURI().startsWith("/auth/logout") || request.getRequestURI().startsWith("/error")) {
            return;
        }
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);

        String refreshTokenFromCookie = getRefreshTokenFromCookie(request);

        boolean isLoggedIn = false;
        boolean isAdmin = false;

        if (refreshTokenFromCookie != null) {
            isLoggedIn = true;
        }

        model.addAttribute("isLoggedIn", isLoggedIn);


        if (isLoggedIn) {
            try {
                model.addAttribute("userName", userApiClient.getUserName());

                String role = userApiClient.getUserRole();
                if ("ADMIN".equals(role)) {
                    isAdmin = true;
                }
            } catch (Exception e) {
                model.addAttribute("userName", "회원");
            }
        } else {
            model.addAttribute("userName", "비회원");
        }

        model.addAttribute("isAdmin", isAdmin);
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