package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.refund.client.RefundApiClient;
import com.nhnacademy._vidiafront.refund.dto.response.RefundHistoryResponse;
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
            Model model
    ) {
        String requestStatus = "ALL".equals(status) ? null : status;

        // 🔹 전체 (탭 카운트용)
        List<RefundHistoryResponse> allRefunds =
                refundApiClient.refundHistory(null);

        // 🔹 선택 상태 리스트 (목록용)
        List<RefundHistoryResponse> refundList =
                refundApiClient.refundHistory(requestStatus);

        Map<String, Long> statusCounts = allRefunds.stream()
                .collect(Collectors.groupingBy(
                        RefundHistoryResponse::returnStatus,
                        Collectors.counting()
                ));

        // 🔹 0 보정
        statusCounts.putIfAbsent("PROCESS", 0L);
        statusCounts.putIfAbsent("ACCEPT", 0L);
        statusCounts.putIfAbsent("REJECT", 0L);

        model.addAttribute("returnList", refundList);
        model.addAttribute("totalReturns", allRefunds.size());
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("currentStatus", status == null ? "ALL" : status);

        return "mypage/order/refundHistory";
    }

}