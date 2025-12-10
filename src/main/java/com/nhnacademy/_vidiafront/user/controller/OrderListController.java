package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.client.OrderItemApiClient;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/order")
@Controller
public class OrderListController {

    private final OrderApiClient orderApiClient;
    private final OrderItemApiClient orderItemApiClient;

    /**
     * 주문관리 페이지
     */
    @GetMapping
    public String orderList(
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            Model model
    ) {

        List<OrderPreviewResponse> allOrders = getSortedOrderList();

        // 상태 카운트 계산 → 기본값 포함
        Map<String, Long> statusCounts = calculateStatusCounts(allOrders);

        String currentStatus = status.toUpperCase();
        model.addAttribute("currentStatus", currentStatus);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("totalOrders", (long) allOrders.size());

        // 필터링
        List<OrderPreviewResponse> filteredOrders = filterOrdersByStatus(allOrders, currentStatus);
        model.addAttribute("orders", filteredOrders);

        // 상태명 한글화
        model.addAttribute("statusNameMap", getStatusKoreanNameMap());

        return "mypage/order/orderList";
    }

    // 주문 목록을 가져와 날짜 기준 최신순 정렬
    private List<OrderPreviewResponse> getSortedOrderList() {
        try {
            List<OrderPreviewResponse> orders = orderApiClient.getOrderPreview();

            if (orders == null) {
                return Collections.emptyList();
            }

            return orders.stream()
                    .sorted(Comparator.comparing(OrderPreviewResponse::createdAt).reversed())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to fetch order preview list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 상태 카운트 계산 + 반드시 기본값 세팅
    private Map<String, Long> calculateStatusCounts(List<OrderPreviewResponse> orders) {

        // 실제 주문 상태 카운트
        Map<String, Long> counts = orders.stream()
                .map(order -> order.deliveryStatus().name())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 기본값 설정 (뷰에서 null 방지)
        counts.putIfAbsent("WAITING", 0L);
        counts.putIfAbsent("SHIPPING", 0L);
        counts.putIfAbsent("DELIVERED", 0L);
        counts.putIfAbsent("CANCELED", 0L);

        // UI 전용 필드
        counts.put("CANCELED_ONLY", counts.get("CANCELED"));
        counts.put("EXCHANGE_RETURN", 0L); // 아직 미구현

        return counts;
    }

    // 선택된 상태에 따라 필터링
    private List<OrderPreviewResponse> filterOrdersByStatus(List<OrderPreviewResponse> allOrders, String status) {

        if ("ALL".equalsIgnoreCase(status)) {
            return allOrders;
        }

        String filter = status.toUpperCase();

        // 취소/교환/반품 → CANCELED 로 묶기
        if ("CANCELED_ONLY".equals(filter) || "EXCHANGE_RETURN".equals(filter)) {
            filter = "CANCELED";
        }

        String finalFilter = filter;

        return allOrders.stream()
                .filter(order -> order.deliveryStatus().name().equalsIgnoreCase(finalFilter))
                .collect(Collectors.toList());
    }

    // UI용 한글 상태명
    private Map<String, String> getStatusKoreanNameMap() {
        return Map.of(
                "WAITING", "배송 준비중",
                "SHIPPING", "배송 중",
                "DELIVERED", "배송 완료",
                "CANCELED", "취소됨"
        );
    }

    /**
     * 구매 확정버튼(마이페이지)
     * */
    @PostMapping("/confirm-item")
    @ResponseBody
    public Map<String, Object> confirmItem(@RequestParam(value = "orderItemId") Long orderItemId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // API 호출을 통해 서버의 OrderItem 상태를 'CONFIRMED'로 변경
            orderItemApiClient.confirmOrderItem(orderItemId);

            response.put("success", true);
            response.put("message", "구매 확정 성공");

        } catch (Exception e) {
            log.error("Failed to confirm order item {}: {}", orderItemId, e.getMessage());
            response.put("success", false);
            response.put("message", "구매 확정 처리 중 오류 발생: " + e.getMessage());
        }
        return response;
    }
}
