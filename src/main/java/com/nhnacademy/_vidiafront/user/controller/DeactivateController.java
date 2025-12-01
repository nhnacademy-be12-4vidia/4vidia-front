package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.DeleteUserRequest;
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
    public String deleteUser(DeleteUserRequest deleteUserRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mypage/deactive/leave";
        }

        userApiClient.deleteUser(deleteUserRequest);
        // todo 로그아웃 시키고, 로그인 페이지로 이동시켜야함
        return "redirect:/auth/login";
    }

}
