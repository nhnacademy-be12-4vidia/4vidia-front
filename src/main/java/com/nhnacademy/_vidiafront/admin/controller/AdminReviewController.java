package com.nhnacademy._vidiafront.admin.controller;
import com.nhnacademy._vidiafront.admin.client.AdminReviewApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminReviewSearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminReviewResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final AdminReviewApiClient adminReviewApiClient;

    /**
     * 관리자 리뷰 목록 페이지 (검색 + 페이징)
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            Model model
    ) {
        AdminReviewSearchRequest cond = new AdminReviewSearchRequest(keyword, rating, page, size);
        PageResponse<AdminReviewResponse> pageResponse = adminReviewApiClient.getReviewPage(cond);

        model.addAttribute("page", pageResponse);
        model.addAttribute("keyword", keyword);
        model.addAttribute("rating", rating);
        model.addAttribute("size", cond.sizeOrDefault());

        return "admin/admin-review-list";
    }

    /**
     * 관리자 리뷰 삭제
     */
    @PostMapping("/{reviewId}/delete")
    public String delete(
            @PathVariable Long reviewId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            RedirectAttributes redirectAttributes
    ) {
        adminReviewApiClient.deleteReview(reviewId);
        redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다.");

        // 원래 보던 검색/페이지로 리다이렉트
        StringBuilder redirectUrl = new StringBuilder("redirect:/admin/reviews");

        boolean hasQuery = false;
        if (keyword != null && !keyword.isBlank()) {
            redirectUrl.append(hasQuery ? "&" : "?")
                    .append("keyword=").append(keyword);
            hasQuery = true;
        }
        if (page != null) {
            redirectUrl.append(hasQuery ? "&" : "?")
                    .append("page=").append(page);
            hasQuery = true;
        }
        if (size != null) {
            redirectUrl.append(hasQuery ? "&" : "?")
                    .append("size=").append(size);
        }

        return redirectUrl.toString();
    }
}