package com.nhnacademy._vidiafront.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.user.client.GradeApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.grade.response.GradePolicyResponse;
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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/profile")
@Controller
public class ProfileController {
    private final UserApiClient userApiClient;
    private final GradeApiClient gradeApiClient;

    /**
     * 회원정보 조회 폼
     * */
    @GetMapping
    public String getUserProfileForm(Model model) {
        UserProfileResponse user = userApiClient.getUserProfile();
        model.addAttribute("user", user);
        model.addAttribute("request", new UpdateUserRequest(user.name(), user.phone()));
        model.addAttribute("gradeName", user.gradeName());

        // ✅ 핵심: SSR에서도 정책 내려주기
        model.addAttribute("gradePolicies", gradeApiClient.getGradePolicies());
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

        } catch (ApiRequestException e) {
            // ✅ 여기서 에러코드로만 메시지 결정
            String message = mapChangePasswordError(e.getErrorCode(), e.getMessage());
            model.addAttribute("errorMessage", message);
            return "mypage/profile/password";

        } catch (Exception e) {
            // 진짜 예상 못한 에러
            log.error("Unexpected error on changePassword", e);
            model.addAttribute("errorMessage", "비밀번호 변경에 실패했습니다.");
            return "mypage/profile/password";
        }
    }
    private String mapChangePasswordError(String errorCode, String fallback) {
        if (errorCode == null) return fallback != null ? fallback : "비밀번호 변경에 실패했습니다.";

        return switch (errorCode) {
            case "U303" -> "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.";
            case "U304" -> "현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.";
            case "U502" -> "비밀번호가 일치하지 않습니다.";
            default -> (fallback != null && !fallback.isBlank())
                    ? fallback
                    : "비밀번호 변경에 실패했습니다.";
        };
    }
    @GetMapping("/grade/policies")
    @ResponseBody
    public List<GradePolicyResponse> gradePolicies() {
        return gradeApiClient.getGradePolicies();
    }








}
