package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindIdRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindPasswordRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.LoginRequest;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthApiClient authApiClient;
    private final BackendApiClient backendApiClient;

    /**
     * 로그인 폼
     * */
    @GetMapping("/login")
    public String loginForm() {
        return "auth/loginForm";
    }

    @PostMapping("/login")
    public String loginForm(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authApiClient.login(loginRequest);
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

        return "redirect:/";

    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        backendApiClient.postNoBody("/api/v1/auth/auth/logout", String.class);

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        deleteCookie("JSESSIONID", response);
        deleteCookie("refresh", response);
        return "redirect:/";
    }
    /**
     * 회원가입 폼
     * */
    @GetMapping("/signup")
    public String signupForm() {
        return "auth/signupForm";
    }

    /**
     * 회원가입
     * */
    @PostMapping("/signup")
    public String signup(UserSignupRequest userSignupRequest) {
        authApiClient.signup(userSignupRequest);
        return "redirect:/auth/login";
    }

    //아이디 찾기
    @GetMapping("/find-id")
    public String findIdPage(Model model) {
        return "auth/find-id";
    }

    @PostMapping("/find-id")
    public String findId(FindIdRequest findIdRequest,
                         BindingResult bindingResult,
                         RedirectAttributes rttr) {

        if (bindingResult.hasErrors()) {
            rttr.addFlashAttribute("error", "입력값을 다시 확인하세요.");
            return "redirect:/auth/find-id";
        }

        try {
            String email = authApiClient.findUserId(findIdRequest);
            rttr.addFlashAttribute("foundEmail", email);  // 성공 메시지
            return "redirect:/auth/find-id";
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "일치하는 회원 정보가 없습니다.");
            return "redirect:/auth/find-id";
        }
    }


    //비밀번호 찾기
    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "auth/find-password";
    }

    @PostMapping("/find-password")
    public String findPassword(FindPasswordRequest findPasswordRequest, RedirectAttributes rttr){
        try {
            authApiClient.findUserPassword(findPasswordRequest);
            rttr.addFlashAttribute("success","입력한 이메일로 임시 비밀번호가 발송되었습니다.");
            return "redirect:/auth/find-password";
        }catch (Exception e){
            rttr.addFlashAttribute("error","일치하는 회원 정보가 없습니다.");
            return "redirect:/auth/find-password";
        }
    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 환경이면 true
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }
}
