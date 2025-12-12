package com.nhnacademy._vidiafront.admin.controller;


import com.nhnacademy._vidiafront.admin.client.AdminUserApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminUserSearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminUserResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserApiClient adminUserClient;

    /**
     * 회원 목록 페이지
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            Model model
    ){
        AdminUserSearchRequest cond = new AdminUserSearchRequest(keyword, status, page, size);
        PageResponse<AdminUserResponse> pageResponse = adminUserClient.getUserPage(cond);

        model.addAttribute("users", pageResponse.content());
        model.addAttribute("page", pageResponse);

        // 검색 값 유지
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "admin/admin-user-list";
    }

    /**
     * 회원 상세 페이지
     * GET /admin/users/{userId}
     */
    @GetMapping("/{userId}")
    public String detail(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            Model model
    ) {
        AdminUserResponse user = adminUserClient.getUser(userId);

        model.addAttribute("user", user);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentStatus", status);

        return "admin/admin-user-detail";
    }


    /**
     * 회원 상태 변경 (폼에서 POST로 호출한다고 가정)
     * POST /admin/users/{userId}/status
     */
    @PostMapping("/{userId}/status")
    public String updateStatus(
            @PathVariable Long userId,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String currentStatus
    ) {
        // 1) 백엔드에 상태 변경 요청
        adminUserClient.updateUserStatus(userId, status);

        // 2) 리다이렉트할 URL을 UriComponentsBuilder로 안전하게 생성
        var builder = org.springframework.web.util.UriComponentsBuilder
                .fromPath("/admin/users")
                .queryParam("page", page);

        if (keyword != null && !keyword.isBlank()) {
            builder.queryParam("keyword", keyword);
        }
        if (currentStatus != null && !currentStatus.isBlank()) {
            builder.queryParam("status", currentStatus);
        }

        String redirectPath = builder.toUriString();

        return "redirect:" + redirectPath;
    }




}
