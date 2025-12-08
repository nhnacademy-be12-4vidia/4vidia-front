package com.nhnacademy._vidiafront.global.controller;

import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserApiClient userApiClient;

    /**
     * layout.html에서 사용 (ex 마이페이지 누르면 열리는 목록 고정시키기?)
     * */
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        // 모든 템플릿에서 ${currentUri}로 접근 가능
        return request.getRequestURI(); 
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());

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

//
//    @ModelAttribute
//    public void addCommonAttributes(Model model, HttpServletRequest request) {
//        model.addAttribute("currentUri", request.getRequestURI());
//
//        HttpSession session = request.getSession(false);
//        boolean isLoggedIn = (session != null && session.getAttribute("accessToken") != null);
//
//        model.addAttribute("isLoggedIn", isLoggedIn);
//
//        if (isLoggedIn) {
//            try {
//                model.addAttribute("userName", userApiClient.getUserName());
//            } catch (ApiRequestException ex) {
//                log.warn("인증 실패 또는 API 요청 오류 발생: {} (URL: {})", ex.getMessage(), request.getRequestURI());
//
//                model.addAttribute("isLoggedIn", false);
//                model.addAttribute("userName", "비회원");
//            } catch (Exception ex) {
//                log.error("프로필 조회 중 예상치 못한 오류 발생: {}", ex.getMessage());
//                model.addAttribute("userName", "비회원");
//            }
//        } else {
//            model.addAttribute("userName", "비회원");
//        }
//    }
}