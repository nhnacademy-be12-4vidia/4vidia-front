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

//
//    @GetMapping
//    public String pointPage(@RequestHeader("X-USER-ID") Long userId,
//                            Model model) {
//
//        // 보유 포인트
//        Integer remain = pointApiClient.getRemain();
//
//        // 소멸 예정 포인트 (30일 기준)
//        Integer expireSoon = pointApiClient.getExpireSoon(30);
//
//        // 포인트 내역 (첫 페이지: page=0, size=10)
//        PointHistoryPageResponse historyPage = pointApiClient.getHistory(0, 10);
//
//        model.addAttribute("remain", remain);
//        model.addAttribute("expireSoon", expireSoon);
//        model.addAttribute("historyPage", historyPage);
//
//        return "mypage/point/pointHistory"; // thymeleaf 템플릿
//    }
@GetMapping
public String pointPage(HttpSession session,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {
    Long userId = (Long) session.getAttribute("userId");

    model.addAttribute("remain", pointApiClient.getRemain());
    model.addAttribute("expireSoon", pointApiClient.getExpireSoon(30));
    model.addAttribute("historyPage", pointApiClient.getHistory(page, 10)); // 추가
    model.addAttribute("userId", userId);

    return "mypage/point/pointHistory";
}
}
