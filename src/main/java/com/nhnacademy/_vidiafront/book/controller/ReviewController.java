package com.nhnacademy._vidiafront.book.controller;

import com.nhnacademy._vidiafront.book.client.ReviewApiClient;
import com.nhnacademy._vidiafront.book.dto.reviews.request.ReviewCreateRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewApiClient reviewApiClient;

    @GetMapping("/books/{bookId}/reviews")
    String showReviewCreateForm(@PathVariable Long bookId, @RequestParam Long orderItemId, Model model) {
        ReviewCreateRequest request = new ReviewCreateRequest(bookId, orderItemId, null, null);

        model.addAttribute("request", request);

        return "review/reviewForm";
    }

    @PostMapping("/books/{bookId}/reviews")
    String createReview(@PathVariable Long bookId, @ModelAttribute ReviewCreateRequest request,
        @RequestParam(name = "images", required = false)
        List<MultipartFile> reviewImageList) throws IOException {

        reviewApiClient.createReview(request, reviewImageList);

        return "redirect:/mypage/order";
    }

}
