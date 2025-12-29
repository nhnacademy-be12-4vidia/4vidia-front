package com.nhnacademy._vidiafront.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.ChangePasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
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
        return "redirect:/mypage/profile";
    }

    /**
     * 비밀번호 수정 폼
     */
    @GetMapping("/password")
    public String changePasswordForm(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest("","",""));
        return "mypage/profile/password";
    }

    /**
     * 비밀번호 수정
     * */
    @PutMapping("/password")
    public String changePassword(@ModelAttribute("changePasswordRequest") ChangePasswordRequest req,
                                 Model model) {


        try {
            userApiClient.changePassword(req);
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", extractProblemDetailMessage(e));
            return "mypage/profile/password";
        }
    }



    private String extractProblemDetailMessage(Throwable e) {
        // 1) RestClient 계열이면 응답 바디가 있음 (가장 정확)
        Throwable cur = e;
        while (cur != null) {
            if (cur instanceof org.springframework.web.client.RestClientResponseException rre) {
                return extractDetailFromJson(rre.getResponseBodyAsString());
            }
            cur = cur.getCause();
        }

        // 2) 응답바디를 못 얻는 경우: 메시지 문자열에 JSON이 포함되어 있으면 그걸 파싱
        String msg = e.getMessage();
        if (msg != null) {
            int start = msg.indexOf('{');
            int end = msg.lastIndexOf('}');
            if (start >= 0 && end > start) {
                String json = msg.substring(start, end + 1);
                String detail = extractDetailFromJson(json);
                if (detail != null && !detail.isBlank()) return detail;
            }
        }

        // 3) 진짜 아무것도 못 뽑으면 마지막 fallback
        return "비밀번호 변경에 실패했습니다.";
    }

    private String extractDetailFromJson(String body) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = om.readTree(body);
            // 백엔드 ProblemDetail 구조: detail에 사람이 읽는 메시지 있음
            return root.path("detail").asText("비밀번호 변경에 실패했습니다.");
        } catch (Exception ignore) {
            return "비밀번호 변경에 실패했습니다.";
        }
    }



}
