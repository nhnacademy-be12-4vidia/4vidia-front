package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.global.dto.PageResponse;
import com.nhnacademy._vidiafront.user.client.LikeApiClient;
import com.nhnacademy._vidiafront.user.dto.like.response.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage/like")
public class LikeController {
    private final LikeApiClient likeApiClient;

    /**
     * 좋아요 리스트 조회 (page)
     */
    @GetMapping
    public String getLikesPage(@PageableDefault(size = 10) Pageable pageable, Model model) {
        PageResponse<LikeResponse> likes = likeApiClient.getLikeListPage(pageable);
        model.addAttribute("likes", likes);

        int startPage = Math.max(1, likes.page() - 4);
        int endPage = Math.min(likes.totalPages(), likes.page() + 5);
        if (endPage == 0) endPage = 1;

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "mypage/like/likeList";
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping
    public String deleteLike(@RequestParam Long bookId) {
        likeApiClient.deleteLike(bookId);
        return "redirect:/mypage/like";
    }

    /**
     * 좋아요 전체 삭제
     */
    @DeleteMapping("/all")
    public String deleteAllLikes() {
        likeApiClient.deleteAllLikes();
        return "redirect:/mypage/like";
    }

}
