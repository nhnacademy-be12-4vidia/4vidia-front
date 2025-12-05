package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.DeleteUserRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/deactivate")
@Controller
public class DeactivateController {
    private final UserApiClient userApiClient;
    private final BackendApiClient backendApiClient;
    private final CartApiClient cartApiClient;

    /**
     * 회원탈퇴 폼
     */
    @GetMapping
    public String deactivateUserForm() {
        return "mypage/deactive/leave";
    }

    /**
     * 회원탈퇴
     */
    @PutMapping
    public String deleteUser(HttpServletRequest request, HttpServletResponse response, DeleteUserRequest deleteUserRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mypage/deactive/leave";
        }

        userApiClient.deleteUser(deleteUserRequest);
        // todo 로그아웃 시키고, 로그인 페이지로 이동시켜야함 (수정필요)
        backendApiClient.postNoBody("/api/v1/auth/auth/logout", String.class);

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        deleteCookie("JSESSIONID", response);
        deleteCookie("refresh", response);

//        cartApiClient.deleteCart();

        return "redirect:/";
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
