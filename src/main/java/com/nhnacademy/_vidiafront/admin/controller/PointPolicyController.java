package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.PointPolicyApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.PointPolicyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/points")
@RequiredArgsConstructor
public class PointPolicyController {

    private final PointPolicyApiClient pointPolicyApiClient;

    @GetMapping
    public String list(Model model){
        model.addAttribute("policies", pointPolicyApiClient.getPointPolicyList());
        return "admin/admin-point";
    }

    @PutMapping("/{policyId}")
    public String update(@PathVariable Long policyId,
                         @Valid PointPolicyRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            pointPolicyApiClient.updatePointPolicy(policyId, request);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/points";
    }


}