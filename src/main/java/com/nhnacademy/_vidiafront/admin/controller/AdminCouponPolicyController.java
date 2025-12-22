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
       쿠폰 정책 생성 (카테고리)
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

    /* ======================================================
       쿠폰 정책 생성 (도서)
    ====================================================== */

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
       정책 활성 / 비활성 토글
    ====================================================== */

    @PatchMapping("/{policyId}/toggle")
    public String toggleActivation(
            @PathVariable Long policyId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        couponApiClient.toggleActivation(policyId);

        return "redirect:/admin/coupons/policies"
                + "?page=" + page
                + "&size=" + size
                + "&status=" + status
                + "&targetType=" + targetType
                + (keyword != null ? "&keyword=" + keyword : "");
    }

    /* ======================================================
       정책 리스트 (메인 화면)
    ====================================================== */

    @GetMapping
    public String listPolicies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
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

        return "admin/admin-coupon-policy-list";
    }
}
