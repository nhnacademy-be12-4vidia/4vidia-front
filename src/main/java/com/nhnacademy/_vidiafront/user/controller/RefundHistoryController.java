package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.global.dto.PageResponse;
import com.nhnacademy._vidiafront.refund.client.RefundApiClient;
import com.nhnacademy._vidiafront.refund.dto.response.RefundCountResponse;
import com.nhnacademy._vidiafront.refund.dto.response.RefundHistoryGroupResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/refund-history")
@Controller
public class RefundHistoryController {
    private final RefundApiClient refundApiClient;

    @GetMapping
    public String refundHistory(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        String viewStatus = (status == null || status.isBlank()) ? "ALL" :status;
        String apiStatus = "ALL".equals(viewStatus) ? null : viewStatus;
        PageResponse<RefundHistoryGroupResponse> allRefunds =
                refundApiClient.refundHistory(apiStatus,page,size);

        RefundCountResponse counts = refundApiClient.getRefundCounts();

        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("PROCESS", counts.process());
        statusCounts.put("APPROVED", counts.approved());

        model.addAttribute("returnList", allRefunds.content());
        model.addAttribute("totalReturns", counts.total());     // ✅ 이제 전체 고정
        model.addAttribute("statusCounts", statusCounts);       // ✅ 이제 탭 숫자 고정

        model.addAttribute("currentStatus",  viewStatus);

        // 페이지네이션 정보 (HTML이 기대하는 변수명 그대로 유지)
        model.addAttribute("page", allRefunds.page());
        model.addAttribute("size", allRefunds.size());
        model.addAttribute("totalPages", allRefunds.totalPages());
        model.addAttribute("isLast", allRefunds.last());

        // first가 없으니 직접 계산
        model.addAttribute("isFirst", allRefunds.page() == 0);


        return "mypage/order/refundHistory";
    }

}