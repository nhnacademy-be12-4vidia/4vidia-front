package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.LikeApiClient;
import com.nhnacademy._vidiafront.user.dto.like.response.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage/like")
public class LikeController {
    private final LikeApiClient likeApiClient;

    /**
     * 좋아요 리스트 조회
     */
    @GetMapping
    public String like(Model model) {
        List<LikeResponse> likes = likeApiClient.getLikeList();
        model.addAttribute("likes", likes);
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


    // 좋아요 등록을 마이페이지에서 하지는 않음 ㅇㅇ
}
