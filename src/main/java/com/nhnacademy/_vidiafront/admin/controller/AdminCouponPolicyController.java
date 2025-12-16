package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminCategoryApiClient;
import com.nhnacademy._vidiafront.admin.client.AdminCouponApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiafront.coupon.dto.CouponPolicyDto;
import com.nhnacademy._vidiafront.coupon.dto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons/policies")
public class AdminCouponPolicyController {

    private final AdminCouponApiClient couponApiClient;
    private final AdminCategoryApiClient categoryApiClient;

    /* ======================================================
       쿠폰 정책 생성
    ====================================================== */

    @GetMapping("/category")
    public String createCategoryForm(Model model) {
        model.addAttribute("categories", categoryApiClient.getCategoryList());
        return "admin/admin-coupon-policy-category";
    }

    @PostMapping("/category")
    public String createCategoryPolicy(CouponPolicyCreateRequest request) {
        couponApiClient.createPolicy(request);
        return "redirect:/admin/coupons/policies";
    }

    @GetMapping("/book")
    public String createBookForm() {
        return "admin/admin-coupon-policy-book";
    }

    @PostMapping("/book")
    public String createBookPolicy(CouponPolicyCreateRequest request) {
        couponApiClient.createPolicy(request);
        return "redirect:/admin/coupons/policies";
    }

    /* ======================================================
       정책 활성 / 비활성
    ====================================================== */

    @PatchMapping("/{policyId}/toggle")
    public String toggle(
            @PathVariable Long policyId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        couponApiClient.toggleActivation(policyId);

        // 🔥 userId 포함해서 다시 같은 화면으로 복귀
        return "redirect:/admin/coupons/policies"
                + "?page=" + page
                + "&size=" + size
                + "&status=" + status
                + "&targetType=" + targetType
                + (keyword != null ? "&keyword=" + keyword : "")
                + (userId != null ? "&userId=" + userId : "");
    }

    /* ======================================================
       메인 화면 (정책 리스트 + 유저 쿠폰 조회)
    ====================================================== */

    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            Model model
    ) {
        // 1️⃣ 쿠폰 정책 리스트
        PageDto<CouponPolicyDto> result =
                couponApiClient.searchPolicies(
                        keyword, status, targetType, page, size
                );

        model.addAttribute("policies", result.content());
        model.addAttribute("page", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("targetType", targetType);
        model.addAttribute("size", size);

        // 2️⃣ 유저 쿠폰 조회 (userId 있을 때만)
        if (userId != null) {
            model.addAttribute("userId", userId);
            model.addAttribute(
                    "userCoupons",
                    couponApiClient.getUserCoupons(userId)
            );
        }

        return "admin/admin-coupon-policy-list";
    }

    /* ======================================================
       쿠폰 발급 (화면 유지)
    ====================================================== */

    @PostMapping("/issue")
    public String issueCoupon(
            @RequestParam Long userId,
            @RequestParam Long policyId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        couponApiClient.issueCouponToUser(userId, policyId);

        // 🔥 발급 후에도 userId 유지해서 다시 조회된 상태로 돌아감
        return "redirect:/admin/coupons/policies"
                + "?userId=" + userId
                + "&page=" + page
                + "&size=" + size
                + "&status=" + status
                + "&targetType=" + targetType
                + (keyword != null ? "&keyword=" + keyword : "");
    }
}
