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

    // 카테고리 쿠폰 생성 폼
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

    // 책 쿠폰 생성 폼
    @GetMapping("/book")
    public String createBookForm() {
        return "admin/admin-coupon-policy-book";
    }

    @PostMapping("/book")
    public String createBookPolicy(CouponPolicyCreateRequest request) {
        couponApiClient.createPolicy(request);
        return "redirect:/admin/coupons/policies";
    }

    // 활성화 비활성화 버튼
    @PatchMapping("/{policyId}/toggle")
    public String toggle(@PathVariable Long policyId) {
        couponApiClient.toggleActivation(policyId);
        return "redirect:/admin/coupons/policies";
    }


    @GetMapping
    public String list(
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
