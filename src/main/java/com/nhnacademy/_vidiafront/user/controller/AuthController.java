package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.admin.dto.request.PointPolicyRequest;
import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.point.client.PointApiClient;
import com.nhnacademy._vidiafront.point.dto.request.PointPolicyRewardRequest;
import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthApiClient authApiClient;
    private final BackendApiClient backendApiClient;
    private final PointApiClient pointApiClient;

    private final CartApiClient  cartApiClient;
    private final UserApiClient userApiClient;

    /**
     * 로그인 폼
     * */
    @GetMapping("/login")
    public String loginForm() {
        return "auth/loginForm";
    }

    @PostMapping("/login")
    public String loginForm(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        deleteCookie("JSESSIONID", response);
        deleteCookie("refresh", response);

        TokenResponse tokenResponse = authApiClient.login(loginRequest);

        String email = loginRequest.email();
        // 휴먼이면 -> 휴먼인증으로 이동
        if (authApiClient.isDormant(email)) {
            request.setAttribute("email", email);
            return "auth/dormant-auth";
        }

        userApiClient.updateLastLoginAt(email);

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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        backendApiClient.postNoBody("/api/v1/auth/auth/logout", String.class);
        cartApiClient.logoutSync();

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
        Long userId = authApiClient.signup(userSignupRequest);
        pointApiClient.rewardByPolicy(new PointPolicyRewardRequest(userId,1L));
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

    @GetMapping("/check-email")
    @ResponseBody
    public Boolean existsEmail(@RequestParam String email) {
        return Boolean.parseBoolean(authApiClient.existsByEmail(email));
    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경이면 true
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

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

    @PostMapping("/dormant/send-code")
    public String sendDormantCode(@RequestParam String email,
                                  @RequestParam String webhookUrl,
                                  RedirectAttributes rttr) {

        authApiClient.sendDormantCode(email, webhookUrl);

        rttr.addAttribute("email", email);
        rttr.addAttribute("sent", true);
        return "redirect:/auth/dormant-auth";
    }

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

    //메일로 휴면 인증
    @GetMapping("/dormant-auth/email")
    public String emailAuthPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "auth/email-dormant-auth";
    }

    @PostMapping("/dormant/send-code/email")
    public String sendEmailCode(@RequestParam String email,
                                @RequestParam String contactEmail,
                                RedirectAttributes rttr) {

        authApiClient.sendDormantCodeByEmail(email, contactEmail);

        rttr.addAttribute("email", email);
        rttr.addAttribute("sent", true);
        return "redirect:/auth/dormant-auth/email";
    }

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
            rttr.addAttribute("email", email);
            rttr.addAttribute("errorMsg", "인증코드가 올바르지 않거나 만료되었습니다.");
            return "redirect:/auth/dormant-auth/email";
        }
    }


}
