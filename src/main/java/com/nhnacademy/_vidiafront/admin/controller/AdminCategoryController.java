package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminCategoryManagementApiClient;
import com.nhnacademy._vidiafront.admin.dto.category.request.CreateCategoryRequest;
import com.nhnacademy._vidiafront.admin.dto.category.request.UpdateCategoryRequest;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryManagementApiClient adminCategoryApiClient;
    private final CategoryApiClient categoryApiClient;

    @GetMapping
    public String listCategories(
            Model model
    ) {
        List<CategoryResponse> categories = categoryApiClient.getCategoryList();
        model.addAttribute("categories", categories);
        return "admin/admin-category";
    }

    @PostMapping
    public String createCategory(
            @ModelAttribute CreateCategoryRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminCategoryApiClient.createCategory(request);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 생성에 실패했습니다.");
            redirectAttributes.addFlashAttribute("errorDetail", e.getResponseBodyAsString());
        }
        return "redirect:/admin/categories";
    }

    @PutMapping("/{categoryId}")
    public String updateCategory(
            @PathVariable Long categoryId,
            @ModelAttribute UpdateCategoryRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminCategoryApiClient.updateCategory(categoryId, request);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 수정에 실패했습니다.");
            redirectAttributes.addFlashAttribute("errorDetail", e.getResponseBodyAsString());
        }
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/{categoryId}")
    public String deleteCategory(
            @PathVariable Long categoryId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminCategoryApiClient.deleteCategory(categoryId);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 409) {
                redirectAttributes.addFlashAttribute("errorMessage", "카테고리를 삭제할 수 없습니다. (연관 데이터 존재)");
                redirectAttributes.addFlashAttribute("errorDetail", e.getResponseBodyAsString());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "삭제 중 오류가 발생했습니다.");
            }
        }
        return "redirect:/admin/categories";
    }
}
