package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminDiscountPolicyApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.DiscountPolicyCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.DiscountPolicyUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.DiscountPolicyResponse;
import com.nhnacademy._vidiafront.book.client.CategoryApiClient;
import com.nhnacademy._vidiafront.book.dto.categories.response.CategoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/discount-policies")
@RequiredArgsConstructor
public class AdminDiscountPolicyController {

    private final AdminDiscountPolicyApiClient discountPolicyApiClient;
    private final CategoryApiClient categoryApiClient;

    private static final String REDIRECT_URL = "redirect:/admin/discount-policies";

    @GetMapping
    public String listPolicies(@RequestParam(required = false) Long categoryId, Model model) {
        List<DiscountPolicyResponse> policies = discountPolicyApiClient.getPolicies(categoryId);
        List<CategoryResponse> categories = categoryApiClient.getFlatCategoryList(); // For category selection in create modal

        model.addAttribute("policies", policies);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        
        return "admin/admin-discount-policy";
    }

    @PostMapping
    public String createPolicy(@ModelAttribute DiscountPolicyCreateRequest request) {
        discountPolicyApiClient.createPolicy(request);
        return REDIRECT_URL;
    }

    @PutMapping("/{id}")
    public String updatePolicy(@PathVariable Long id, @ModelAttribute DiscountPolicyUpdateRequest request) {
        discountPolicyApiClient.updatePolicy(id, request);
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String deletePolicy(@PathVariable Long id) {
        discountPolicyApiClient.deletePolicy(id);
        return REDIRECT_URL;
    }
}
