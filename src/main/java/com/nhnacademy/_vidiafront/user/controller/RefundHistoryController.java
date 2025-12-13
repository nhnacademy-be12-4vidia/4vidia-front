package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.refund.client.RefundApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/refund-history")
@Controller
public class RefundHistoryController {
    private final RefundApiClient refundApiClient;

    @GetMapping
    public String refundHistory(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "3months") String period,
            Model model
    ) {
        // 1. 임시 데이터
        List<RefundHistoryResponse> refundList = Arrays.asList(
                // Item 1: 나니아 연대기
                new RefundHistoryResponse(
                        1L,
                        1L,
                        "ORD-20241215-004",
                        LocalDate.of(2024, 12, 15),
                        "나니아 연대기",
                        "C.S. 루이스",
                        18000,
                        1,
                        "https://placehold.co/80x112?text=Book12", // 이미지 URL 예시
                        LocalDate.of(2024, 12, 20),
                        RefundStatus.REQUESTED, // 반품신청
                        "단순 변심",
                        "환불",
                        15000,
                        3000,
                        "KB국민은행 123-456-789012 (홍길동)",
                        null,
                        null
                ),

                // Item 2: 해리포터와 비밀의 방
                new RefundHistoryResponse(
                        2L,
                        2L,
                        "ORD-20241210-006",
                        LocalDate.of(2024, 12, 10),
                        "해리포터와 비밀의 방",
                        "J.K. 롤링",
                        16000,
                        1,
                        "https://placehold.co/80x112?text=Book13",
                        LocalDate.of(2024, 12, 18),
                        RefundStatus.REQUESTED, // 반품신청
                        "상품 불량/파손",
                        "환불",
                        16000,
                        0,
                        "KB국민은행 123-456-789012 (홍길동)",
                        null,
                        null
                ),

                // Item 3: 반지의 제왕: 왕의 귀환
                new RefundHistoryResponse(
                        3L,
                        3L,
                        "ORD-20241205-007",
                        LocalDate.of(2024, 12, 5),
                        "반지의 제왕: 왕의 귀환",
                        "J.R.R. 톨킨",
                        22000,
                        1,
                        "https://placehold.co/80x112?text=Book14",
                        LocalDate.of(2024, 12, 12),
                        RefundStatus.COMPLETED, // 반품완료
                        "상품이 설명과 다름",
                        "환불",
                        22000,
                        0,
                        "KB국민은행 123-456-789012 (홍길동)",
                        null,
                        "1234-5678-9012" // 회수 운송장 (완료건 예시)
                ),

                // Item 4: 호빗
                new RefundHistoryResponse(
                        4L,
                        4L,
                        "ORD-20241201-008",
                        LocalDate.of(2024, 12, 1),
                        "호빗",
                        "J.R.R. 톨킨",
                        15000,
                        2,
                        "https://placehold.co/80x112?text=Book15",
                        LocalDate.of(2024, 12, 8),
                        RefundStatus.REJECTED, // 반품거부
                        "단순 변심",
                        "환불",
                        0,
                        0,
                        "KB국민은행 123-456-789012 (홍길동)",
                        "반품 가능 기간(7일)이 초과되었습니다.",
                        null
                )
        );


        // 2. 상태별 카운트 계산 (탭에 숫자 표시용)
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("REQUESTED", refundList.stream().filter(r -> r.returnStatus() == RefundStatus.REQUESTED).count());
        statusCounts.put("COMPLETED", refundList.stream().filter(r -> r.returnStatus() == RefundStatus.COMPLETED).count());
        statusCounts.put("REJECTED", refundList.stream().filter(r -> r.returnStatus() == RefundStatus.REJECTED).count());

        // 3. 필터링 로직 (실제 DB 연동 전 임시 필터링)
        List<RefundHistoryResponse> filteredList = refundList;
        if (!"ALL".equals(status)) {
            filteredList = refundList.stream()
                    .filter(item -> item.returnStatus().name().equals(status))
                    .toList();
        }

        // 4. 모델에 데이터 추가
        model.addAttribute("returnList", filteredList); // 뷰에서 th:each="item : ${returnList}" 로 사용
        model.addAttribute("totalReturns", refundList.size());
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentPeriod", period);

        // 뷰에서 Enum 한글명 표시를 위한 맵 (statusMap)
        Map<String, String> statusMap = Map.of(
                "REQUESTED", "반품신청",
                "COMPLETED", "반품완료",
                "REJECTED", "반품거부"
        );
        model.addAttribute("statusMap", statusMap);

        log.info("Loaded {} refund items based on filter: status={}, period={}", filteredList.size(), status, period);

        return "mypage/order/refundHistory";
    }
}