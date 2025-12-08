package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.GradePolicyApiClient;
import com.nhnacademy._vidiafront.admin.client.PointPolicyApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.GradePolicyUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.PointPolicyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/grades")
@RequiredArgsConstructor
public class GradePolicyController {

    private final GradePolicyApiClient gradePolicyApiClient;

    @GetMapping
    public String list(Model model){
        model.addAttribute("grades", gradePolicyApiClient.getGradePolicyList());
        return "admin/admin-grade";
    }

    @PutMapping("/{gradeId}")
    public String update(@PathVariable Long gradeId,
                         GradePolicyUpdateRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            gradePolicyApiClient.updatePointPolicy(gradeId, request);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/admin/grades";
    }


}