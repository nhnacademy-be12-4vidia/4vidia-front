package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * GET  /mypage/profile
 * */
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
    public String getUserProfile(Model model) {
        UserProfileResponse user = userApiClient.getUserProfile();
        model.addAttribute("user", user);
        model.addAttribute("request", new UpdateUserRequest(user.name(), user.phone()));
        return "/user/mypage/profile/info";
    }


}
