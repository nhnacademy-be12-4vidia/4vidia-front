package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.ChangePasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/profile")
@Controller
public class ProfileController {
    private final UserApiClient userApiClient;

    /**
     * 회원정보 조회 폼
     * */
    @GetMapping
    public String getUserProfileForm(Model model) {
        UserProfileResponse user = userApiClient.getUserProfile();
        model.addAttribute("user", user);
        model.addAttribute("request", new UpdateUserRequest(user.name(), user.phone()));
        model.addAttribute("gradeName", user.gradeName());
        return "mypage/profile/info";
    }

    /**
     * 회원정보 수정
     * */
    @PutMapping
    public String updateUserProfile(UpdateUserRequest updateUserRequest,
                                    RedirectAttributes redirectAttributes) {
        UserProfileResponse user = userApiClient.updateUserProfile(updateUserRequest);
        log.info("user profile updated: {}", user);

        redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
        return "redirect:/mypage/profile"; // todo: 회원정보 조회,수정을 한페이지에서 하는데 너무 짜침, 수정도 너무 쉬움
    }

    /**
     * 비밀번호 수정 폼
     */
    @GetMapping("/password")
    public String changePasswordForm() {
        return "mypage/profile/password";
    }

    /**
     * 비밀번호 수정
     * */
    @PutMapping("/password")
    public String changePassword(ChangePasswordRequest changePasswordRequest,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mypage/profile/password";
        }

        userApiClient.changePassword(changePasswordRequest);
        return "redirect:/mypage/profile";
    }

}
