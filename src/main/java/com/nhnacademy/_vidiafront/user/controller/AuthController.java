package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindIdRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindPasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 로그인 폼
     * */
    @GetMapping("/login")
    public String loginForm() {
        return "auth/loginForm";
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
        return "/auth/find-id";
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
        return "/auth/find-password";
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


}
