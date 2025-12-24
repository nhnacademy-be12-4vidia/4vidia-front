package com.nhnacademy._vidiafront.point.controller;

import com.nhnacademy._vidiafront.point.client.PointApiClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/point")
public class PointController {

    private final PointApiClient pointApiClient;

    @GetMapping
    public String pointPage(HttpSession session,
                            @RequestParam(defaultValue = "ALL") String category,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {

        // 1. 기간 기본값 설정 (최근 3개월)
        LocalDate end = (to != null) ? to : LocalDate.now();
        LocalDate start = (from != null) ? from : end.minusMonths(3);

        // 2. 날짜 역전 방지
        if (start.isAfter(end)) {
            LocalDate tmp = start;
            start = end;
            end = tmp;
        }

        // 3. API 호출 (계산된 start, end 전달)
        model.addAttribute("remain", pointApiClient.getPointTotal().totalPrice());
        model.addAttribute("expireSoon", pointApiClient.getExpireSoon(7));
        model.addAttribute("historyPage", pointApiClient.getHistoryPage(category, start, end, page, 10));

        // 4. 뷰 전달 (input type="date"는 yyyy-MM-dd 문자열을 필요로 함)
        model.addAttribute("category", category);
        model.addAttribute("from", start);
        model.addAttribute("to", end);

        return "mypage/point/pointHistory";
    }
}