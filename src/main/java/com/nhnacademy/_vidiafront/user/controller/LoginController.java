package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.CompleteProfileRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.LoginRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.PaycoCodeRequest;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
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
        return "auth/loginForm";
    }

    @GetMapping("/login/payco")
    public void redirectToPayco(HttpServletResponse response) throws IOException {
        response.sendRedirect(paycoLoginUrl);
    }
    /**
     * 로그인
     */
    @PostMapping("/auth/login")
    public String loginForm(LoginRequest loginRequest,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        authApiClient.deleteCookie("SES", response);
        authApiClient.deleteCookie("AUT", response);

        TokenResponse tokenResponse = authApiClient.login(loginRequest);

        System.out.println(tokenResponse.accessToken());
        System.out.println(tokenResponse.refreshUuid());
        String email = loginRequest.email();
        // 휴먼이면 -> 휴먼인증으로 이동
//        if (authApiClient.isDormant(email)) {
//            request.setAttribute("email", email);
//            return "auth/dormant-auth";
//        }

        authApiClient.updateLastLoginAt(email);

        String refreshUuid = tokenResponse.refreshUuid();
        String accessToken = tokenResponse.accessToken();

        Cookie accessCookie = new Cookie("SES", accessToken);
        accessCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
        accessCookie.setSecure(false);             // HTTPS 환경이면 true
        accessCookie.setPath("/");
        accessCookie.setMaxAge(30 * 60);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("AUT", refreshUuid);
        refreshCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
        refreshCookie.setSecure(false);             // HTTPS 환경이면 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }

    @GetMapping("/login/oauth2/code/payco")
    public String paycoLoginCallback(@RequestParam String code,
                                     @RequestParam(required = false) String state, HttpServletResponse response) {
        TokenResponse tokenResponse = authApiClient.paycoCallback(new PaycoCodeRequest(code, state));

        String accessToken = tokenResponse.accessToken();
        String refreshToken = tokenResponse.refreshUuid();

        Cookie accessCookie = new Cookie("SES", accessToken);
        accessCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
        accessCookie.setSecure(false);             // HTTPS 환경이면 true
        accessCookie.setPath("/");
        accessCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(accessCookie);


        Cookie refreshCookie = new Cookie("AUT", refreshToken);
        refreshCookie.setHttpOnly(true);           // 브라우저 JS 접근 불가
        refreshCookie.setSecure(false);             // HTTPS 환경이면 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(30 * 24 * 60 * 60); // 30일
        response.addCookie(refreshCookie);


        return "redirect:/";
    }

    /**
     * 로그아웃
     */
    @PostMapping("/auth/logout")
    public String logout(HttpServletResponse response) {
        String userId = authApiClient.logout();
        log.info("로그아웃 함 -> User id: {}", userId);
        cartApiClient.logoutSync();


        deleteCookie("AUT", response);
        deleteCookie("SES", response);

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
        deleteCookie("AUT", response);
        deleteCookie("SES", response);

        return "redirect:/login/payco";
    }


    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");     // 로그인 때 설정한 path와 반드시 동일
        cookie.setMaxAge(0);     // 즉시 만료
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 로그인 때 false면 여기서도 false

        response.addCookie(cookie);
    }
}
