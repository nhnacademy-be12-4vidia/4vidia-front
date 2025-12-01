package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * GET  /auth/login
 * GET  /auth/signup
 * POST /auth/signup
 * */
@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthApiClient authApiClient;

    /**
     * 로그인 폼
     * */
    @GetMapping("/login")
    public String loginForm() {
        return "/auth/loginForm";
    }

    /**
     * 회원가입 폼
     * */
    @GetMapping("/signup")
    public String signupForm() {
        return "/auth/signupForm";
    }

    /**
     * 회원가입
     * */
    @PostMapping("/signup")
    public String signup(UserSignupRequest userSignupRequest) {
        authApiClient.signup(userSignupRequest);
        return "redirect:/auth/login";
    }

}
