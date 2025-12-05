package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.admin.dto.request.PointPolicyRequest;
import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.point.client.PointApiClient;
import com.nhnacademy._vidiafront.point.dto.request.PointPolicyRewardRequest;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthApiClient authApiClient;
    private final BackendApiClient backendApiClient;
    private final PointApiClient pointApiClient;

    private final CartApiClient  cartApiClient;
    /**
     * 로그인 폼
     * */
    @GetMapping("/login")
    public String loginForm() {
        return "auth/loginForm";
    }

    @PostMapping("/login")
    public String loginForm(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        try {

            // 휴먼이면 -> 휴먼인증으로 이동
            if (authApiClient.isDormant(loginRequest)) {
                request.setAttribute("email", loginRequest.email());
                return "auth/dormant-auth";
            }

            // 아니면 로그인 ㄱ
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

            cartApiClient.loginSync();

            return "redirect:/";
        } catch (HttpClientErrorException e) { // todo : 아래는 어케 쓰는거지?  백엔드에서 던진 예외처리 메세지 어케 씀??
            // 1. HTTP 통신 예외 Catch
            int statusCode = e.getRawStatusCode(); // HTTP 상태 코드 확인

            if (statusCode == 401 || statusCode == 404) {
                // 401 Unauthorized (비밀번호 불일치) 또는 404 Not Found (이메일 없음) 일 때
                request.setAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
                return "auth/loginForm";
            } else if (statusCode == 403) {
                // 403 Forbidden 일 때 (백엔드의 UserNotFoundException -> 탈퇴 계정)
                // 백엔드에서 탈퇴 회원을 예외로 처리했으므로, 여기서 잡아 메시지를 보여줍니다.
                request.setAttribute("errorMessage", "탈퇴한 회원입니다. 다시 가입해 주세요.");
                return "auth/loginForm";
            } else {
                // 기타 HTTP 에러
                log.error("Login HTTP Error: {}", e.getMessage());
                request.setAttribute("errorMessage", "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                return "auth/loginForm";
            }
        } catch (Exception e) {
            // 네트워크 오류 등 기타 예외
            log.error("Login Unknown Error: {}", e.getMessage());
            request.setAttribute("errorMessage", "로그인 중 예상치 못한 오류가 발생했습니다."); // todo 왜 전부 이걸로 뜨지?
            return "auth/loginForm";
        }
    }




    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        backendApiClient.postNoBody("/api/v1/auth/auth/logout", String.class);


        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        deleteCookie("JSESSIONID", response);
        deleteCookie("refresh", response);

//        cartApiClient.logoutSync();
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
}
