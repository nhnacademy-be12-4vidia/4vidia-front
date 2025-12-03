package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.ChangePasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        return "mypage/profile/info";
    }

    /**
     * 회원정보 수정
     * */
    @PutMapping
    public String updateUserProfile(UpdateUserRequest updateUserRequest,
                                    Model model) {
//        String result = userApiClient.updateUserProfile(updateUserRequest);
//        log.info("result : {}", result);
        // todo: 궁금한거 - 위에서 받은 result값 처럼 로그로 찍기위해 string으로 리턴받아야하는지? void로 바꾸면 안되는지?
        //  회원정보 수정 로직...

        UserProfileResponse user = userApiClient.updateUserProfile(updateUserRequest);
        model.addAttribute("user", user);
        log.debug("회원정보 수정 성공");
        return "redirect:/mypage/profile";
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
        // todo : 비밀번호 수정 후, 로그아웃 시키기
        return "redirect:/mypage/profile";
    }




}
