package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminCouponApiClient;
import com.nhnacademy._vidiafront.admin.client.AdminUserApiClient;
import com.nhnacademy._vidiafront.admin.dto.response.AdminUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users/coupons")
public class AdminCouponUserController {

    private final AdminCouponApiClient couponApiClient;
    private final AdminUserApiClient userApiClient;

    /* =========================
       유저 쿠폰 발급 페이지
       GET /admin/users/coupons/{userId}
    ========================== */
    @GetMapping("/{userId}")
    public String userCouponPage(
            @PathVariable Long userId,
            Model model
    ) {
        // 1️⃣ 유저 정보
        AdminUserResponse user = userApiClient.getUser(userId);

        // 2️⃣ 발급 가능한 쿠폰 정책
        model.addAttribute("user", user);
        model.addAttribute(
                "issuablePolicies",
                couponApiClient.getIssuablePolicies(userId)
        );

        return "admin/admin-coupon-user-list";
    }

    /* =========================
       재고 제한 쿠폰 발급
    ========================== */
    @PostMapping("/{userId}/coupons/{policyId}/issue")
    @ResponseBody
    public void issueLimited(
            @PathVariable Long userId,
            @PathVariable Long policyId
    ) {
        couponApiClient.issueCouponToUser(userId, policyId);
    }

    /* =========================
       무제한 쿠폰 발급
    ========================== */
    @PostMapping("/{userId}/coupons/{policyId}/event-issue")
    @ResponseBody
    public void issueEvent(
            @PathVariable Long userId,
            @PathVariable Long policyId
    ) {
        couponApiClient.issueEventCouponToUser(userId, policyId);
    }
}
