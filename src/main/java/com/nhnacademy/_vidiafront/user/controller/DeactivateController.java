package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.user.client.AuthApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.DeleteUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/deactivate")
@Controller
public class DeactivateController {
    private final UserApiClient userApiClient;
    private final CartApiClient cartApiClient;
    private final AuthApiClient authApiClient;

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
    public String deleteUser(HttpServletRequest request,
                             HttpServletResponse response,
                             DeleteUserRequest deleteUserRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mypage/deactive/leave";
        }

        userApiClient.deleteUser(deleteUserRequest);
        String userId = authApiClient.logout();
        log.info("회원탈퇴 후 로그아웃함  userId: {}", userId);
        cartApiClient.deleteCart();

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        authApiClient.deleteCookie("JSESSIONID", response);
        authApiClient.deleteCookie("refresh", response);

        return "redirect:/";
    }

}
