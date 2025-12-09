package com.nhnacademy._vidiafront.point.controller;

import com.nhnacademy._vidiafront.point.client.PointApiClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/point")
public class PointController {

    private final PointApiClient pointApiClient;

    @GetMapping
    public String pointPage(HttpSession session,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        Long userId = (Long) session.getAttribute("userId");

        // TODO user.point 가져오기
        model.addAttribute("remain", pointApiClient.getPointTotal().totalPrice());
        model.addAttribute("expireSoon", pointApiClient.getExpireSoon(7));
        model.addAttribute("historyPage", pointApiClient.getHistoryPage(page, 10)); // 추가
        model.addAttribute("userId", userId);

        return "mypage/point/pointHistory";
    }
}
