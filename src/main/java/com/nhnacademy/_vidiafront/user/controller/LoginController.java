package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.global.filter.JwtUtil;
import com.nhnacademy._vidiafront.point.client.PointApiClient;
import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.CompleteProfileRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.LoginRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.PaycoCodeRequest;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final JwtUtil jwtUtil;
    private final PointApiClient pointApiClient;
    private final UserApiClient userApiClient;
    private final CartApiClient cartApiClient;
    private final AuthApiClient authApiClient;

    @Value("${auth.payco.login-url}")
    private String paycoLoginUrl;

    /**
     * 로그인 폼
     *
     */
    @GetMapping("/auth/login")
    public String loginForm(Model model) {
        model.addAttribute("paycoLoginUrl", paycoLoginUrl);
        return "auth/loginForm";
    }

    /**
     * 로그인
     */
    @PostMapping("/auth/login")
    public String loginForm(LoginRequest loginRequest,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        authApiClient.deleteCookie("JSESSIONID", response);
        authApiClient.deleteCookie("refresh", response);

        TokenResponse tokenResponse = authApiClient.login(loginRequest);

        String email = loginRequest.email();
        // 휴먼이면 -> 휴먼인증으로 이동
        if (authApiClient.isDormant(email)) {
            request.setAttribute("email", email);
            return "auth/dormant-auth";
        }

        authApiClient.updateLastLoginAt(email);

        String accessToken = tokenResponse.accessToken();
        String refreshToken = tokenResponse.refreshToken();


        HttpSession session = request.getSession(true);
        session.setAttribute("accessToken", accessToken);

        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        refreshCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
        refreshCookie.setSecure(false);             // HTTPS 환경이면 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshCookie);

        cartApiClient.loginSync();

        return "redirect:/";
    }

    @GetMapping("/login/oauth2/code/payco")
    public String paycoLoginCallback(@RequestParam String code,
                                     @RequestParam(required = false) String state, HttpServletResponse response, HttpServletRequest request) {
        TokenResponse tokenResponse = authApiClient.paycoCallback(new PaycoCodeRequest(code, state));
        String accessToken = tokenResponse.accessToken();
        String refreshToken = tokenResponse.refreshToken();


        HttpSession session = request.getSession(true);
        session.setAttribute("accessToken", accessToken);

        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        refreshCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
        refreshCookie.setSecure(false);             // HTTPS 환경이면 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshCookie);
        cartApiClient.loginSync();

        String userStatus = jwtUtil.getStatus(accessToken);

        if ("TEMP" .equals(userStatus)) {
            return "redirect:/auth/complete-profile";
        }

        return "redirect:/";
    }

    /**
     * 로그아웃
     */
    @PostMapping("/auth/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        String userId = authApiClient.logout();
        log.info("로그아웃 함 -> User id: {}", userId);
        cartApiClient.logoutSync();

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        authApiClient.deleteCookie("JSESSIONID", response);
        authApiClient.deleteCookie("refresh", response);

        return "redirect:/";
    }

    @GetMapping("/complete-profile")
    public String completeProfileForm() {
        // 실제 구현 시, 이름, 전화번호, 생년월일 등을 입력받는 폼을 제공해야 합니다.
        return "auth/completeProfileForm"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/complete-profile")
    public String completeProfile(@ModelAttribute CompleteProfileRequest completeProfileRequest, HttpServletRequest request, HttpServletResponse response) {
        userApiClient.completeProfile(completeProfileRequest);
        authApiClient.logout();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        authApiClient.deleteCookie("JSESSIONID", response);
        authApiClient.deleteCookie("refresh", response);

        return "redirect:/";
    }
}
