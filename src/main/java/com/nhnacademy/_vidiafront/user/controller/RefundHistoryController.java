package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.refund.client.RefundApiClient;
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
            Model model
    ) {
        // 1. 딱 한 번만 호출해서 모든 데이터를 가져옵니다.
        List<RefundHistoryGroupResponse> allRefunds = refundApiClient.refundHistory(null);

        // 2. 전체 리스트를 이용해 상태별 카운트를 계산합니다. (메모리 연산)
        Map<String, Long> statusCounts = allRefunds.stream()
                .collect(Collectors.groupingBy(
                        r -> r.refundStatus().name(),
                        Collectors.counting()
                ));

        // 기본값 설정
        statusCounts.putIfAbsent("PROCESS", 0L);
        statusCounts.putIfAbsent("APPROVED", 0L);

        // 3. 필터링 로직: 선택된 상태가 있으면 필터링하고, 없거나 "ALL"이면 전체를 사용합니다.
        List<RefundHistoryGroupResponse> refundList;
        if (status == null || "ALL".equals(status)) {
            refundList = allRefunds;
        } else {
            refundList = allRefunds.stream()
                    .filter(r -> status.equals(r.refundStatus().name()))
                    .toList();
        }

        model.addAttribute("returnList", refundList);
        model.addAttribute("totalReturns", allRefunds.size());
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("currentStatus", status == null ? "ALL" : status);

        return "mypage/order/refundHistory";
    }

}