package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminCategoryApiClient;
import com.nhnacademy._vidiafront.admin.client.AdminCouponApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyUpdateRequest;
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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("policies", couponApiClient.getPolicyList());
        return "admin/admin-coupon-policy-list";
    }

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

    @GetMapping("/{policyId}/edit")
    public String editPolicyForm(@PathVariable Long policyId, Model model) {
        model.addAttribute("policy", couponApiClient.getPolicy(policyId));
        model.addAttribute("categories", categoryApiClient.getCategoryList());
        return "admin/admin-coupon-policy-edit";
    }

    @PostMapping("/{policyId}/edit")
    public String editPolicy(
            @PathVariable Long policyId,
            CouponPolicyUpdateRequest request
    ) {
        couponApiClient.updatePolicy(policyId, request);
        return "redirect:/admin/coupons/policies";
    }



}
