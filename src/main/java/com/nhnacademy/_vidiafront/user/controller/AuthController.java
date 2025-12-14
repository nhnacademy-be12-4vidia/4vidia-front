package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.point.client.PointApiClient;
import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindIdRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindPasswordRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.LoginRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.PaycoCodeRequest;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthApiClient authApiClient;



    /**
     * 회원가입 폼
     *
     */
    @GetMapping("/signup")
    public String signupForm() {
        return "auth/signupForm";
    }

    /**
     * 회원가입
     *
     */
    @PostMapping("/signup")
    public String signup(UserSignupRequest userSignupRequest) {
        authApiClient.signup(userSignupRequest);
        return "redirect:/auth/login";
    }

    /**
     * 아이디 찾기 폼
     */
    @GetMapping("/find-id")
    public String findIdPage() {
        return "auth/find-id";
    }

    /**
     * 아이디 찾기
     */
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

    /**
     * 비밀번호 찾기 폼
     */
    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "auth/find-password";
    }

    /**
     * 비밀번호 찾기(새로 발급)
     */
    @PostMapping("/find-password")
    public String findPassword(FindPasswordRequest findPasswordRequest,
                               RedirectAttributes rttr) {
        try {
            authApiClient.issueNewPassword(findPasswordRequest);
            rttr.addFlashAttribute("success", "입력한 이메일로 임시 비밀번호가 발송되었습니다.");
            return "redirect:/auth/find-password";
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "일치하는 회원 정보가 없습니다.");
            return "redirect:/auth/find-password";
        }
    }

    /**
     * 이메일 여부 조회
     */
    @GetMapping("/check-email")
    @ResponseBody
    public Boolean existsEmail(@RequestParam String email) {
        return Boolean.parseBoolean(authApiClient.existsByEmail(email));
    }

    /**
     * 휴먼인증 페이지 폼
     */
    @GetMapping("/dormant-auth")
    public String dormantAuthPage(@RequestParam(required = false) String email,
                                  @RequestParam(required = false) Boolean sent,
                                  @RequestParam(required = false) Boolean error,
                                  @RequestParam(required = false) Boolean success,
                                  Model model) {
        model.addAttribute("email", email);
        model.addAttribute("sent", sent);
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "auth/dormant-auth";
    }

    /**
     * 두레이 인증코드 전송
     */
    @PostMapping("/dormant/send-code")
    public String sendDormantCode(@RequestParam String email,
                                  @RequestParam String webhookUrl,
                                  RedirectAttributes rttr) {

        authApiClient.sendDormantCode(email, webhookUrl);

        rttr.addAttribute("email", email);
        rttr.addAttribute("sent", true);
        return "redirect:/auth/dormant-auth";
    }

    /**
     * 두레이 인증코드 인증
     */
    @PostMapping("/dormant/verify")
    public String verifyDormantCode(@RequestParam String email,
                                    @RequestParam String code,
                                    RedirectAttributes rttr) {
        try {
            authApiClient.verifyDormantCode(email, code);
            rttr.addAttribute("success", true);
            return "redirect:/auth/dormant-auth";

        } catch (Exception e) {
            String msg;

            // 메시지 상세 가능
            if (e.getMessage().contains("EXPIRED")) {
                msg = "인증코드가 만료되었습니다. 다시 요청해주세요.";
            } else {
                msg = "올바르지 않은 인증코드입니다.";
            }

            rttr.addAttribute("email", email);
            rttr.addAttribute("errorMsg", msg);
            return "redirect:/auth/dormant-auth";
        }
    }

    /**
     * 메일로 휴면 인증 페이지 작성
     */
    @GetMapping("/dormant-auth/email")
    public String emailAuthPage(@RequestParam String email,
                                Model model) {
        model.addAttribute("email", email);
        return "auth/email-dormant-auth";
    }

    /**
     * 메일로 인증코드 전송
     */
    @PostMapping("/dormant/send-code/email")
    public String sendEmailCode(@RequestParam String email,
                                @RequestParam String contactEmail,
                                RedirectAttributes rttr) {
        authApiClient.sendDormantCodeByEmail(email, contactEmail);

        rttr.addAttribute("email", email);
        rttr.addAttribute("sent", true);
        return "redirect:/auth/dormant-auth/email";
    }

    /**
     * 메일로 인증코드 확인
     */
    @PostMapping("/dormant/verify/email")
    public String verifyEmailCode(@RequestParam String email,
                                  @RequestParam String code,
                                  RedirectAttributes rttr) {
        try {
            authApiClient.verifyDormantCode(email, code);
            rttr.addAttribute("email", email);
            rttr.addAttribute("success", true);
            return "redirect:/auth/dormant-auth/email";

        } catch (Exception e) {
            String msg;

            // 메시지 상세 가능
            if (e.getMessage().contains("EXPIRED")) {
                msg = "인증코드가 만료되었습니다. 다시 요청해주세요.";
            } else {
                msg = "올바르지 않은 인증코드입니다.";
            }

            rttr.addAttribute("email", email);
            rttr.addAttribute("errorMsg", msg);
            return "redirect:/auth/dormant-auth/email";
        }
    }
}
