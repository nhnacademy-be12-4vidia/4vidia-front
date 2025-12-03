package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.order.config.OrderApiClient;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/order")
@Controller
public class OrderListController {
    private final OrderApiClient orderApiClient;

    /**
     * 주문관리 페이지 폼 (임시)
     * */
    @GetMapping
    public String orderList(@RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
                            Model model) {

        List<OrderPreviewResponse> allOrders = getSortedOrderList();

        Map<String, Long> statusCounts = calculateStatusCounts(allOrders);

        String currentStatus = status.toUpperCase();
        model.addAttribute("currentStatus", currentStatus);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("totalOrders", (long) allOrders.size());

        List<OrderPreviewResponse> filteredOrders = filterOrdersByStatus(allOrders, currentStatus);
        model.addAttribute("orders", filteredOrders);

        model.addAttribute("statusNameMap", getStatusKoreanNameMap());

        return "mypage/order/orderList";
    }

    private List<OrderPreviewResponse> getSortedOrderList() {
        List<OrderPreviewResponse> orders = orderApiClient.getOrderPreview();

        return orders.stream()
                .sorted(Comparator.comparing(OrderPreviewResponse::createdAt).reversed())
                .collect(Collectors.toList());
    }

    // 주문 통계 Map 계산 (Map<String(Enum Name), Long(Count)>)
    private Map<String, Long> calculateStatusCounts(List<OrderPreviewResponse> orders) {
        Map<String, Long> counts = orders.stream()
                .map(order -> order.deliveryStatus().name())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long canceledCount = counts.getOrDefault("CANCELED", 0L);

        // DB에 CANCELED만 있다면, 임의로 취소와 교환/반품을 분할하거나 합산하여 전달합니다.
        // 여기서는 CANCELED를 합산하고, 두 개 탭에는 0을 넣어둡니다.
        counts.put("CANCELED_ONLY", canceledCount);
        counts.put("EXCHANGE_RETURN", 0L);

        return counts;
    }


    // 선택된 상태에 따라 목록 필터링
    private List<OrderPreviewResponse> filterOrdersByStatus(List<OrderPreviewResponse> allOrders, String status) {
        if ("ALL".equalsIgnoreCase(status)) {
            return allOrders;
        }

        final String finalFilterStatus;

        String upperStatus = status.toUpperCase();

        // 조건에 따라 최종 필터링 상태를 결정
        if ("CANCELED_ONLY".equalsIgnoreCase(upperStatus) || "EXCHANGE_RETURN".equalsIgnoreCase(upperStatus)) {
            // CANCELED_ONLY 또는 EXCHANGE_RETURN 탭을 눌렀을 경우, 백엔드 상태는 CANCELED로 필터링
            finalFilterStatus = "CANCELED";
        } else {
            // 그 외의 경우 (WAITING, SHIPPING, DELIVERED)는 입력 상태 그대로 사용
            finalFilterStatus = upperStatus;
        }

        // finalFilterStatus는 선언 후 값이 변경된 적이 없으므로,
        return allOrders.stream()
                .filter(order -> order.deliveryStatus().name().equalsIgnoreCase(finalFilterStatus))
                .collect(Collectors.toList());
    }

    // 한글 상태명을 쉽게 사용하기 위한 Map 추가
    private Map<String, String> getStatusKoreanNameMap() {
        return Map.of(
                "WAITING", "배송 준비중",
                "SHIPPING", "배송 중",
                "DELIVERED", "배송 완료",
                "CANCELED", "취소됨"
                // CANCELED_ONLY와 EXCHANGE_RETURN은 탭 레이블이므로 굳이 맵에 넣지 않아도 됨
        );
    }
}
