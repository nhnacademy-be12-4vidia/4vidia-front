package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.client.OrderItemApiClient;
import com.nhnacademy._vidiafront.order.dto.ConfirmStatus;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
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

    // 상태 카운트 계산 + 반드시 기본값 세팅 (수정됨)
    private Map<String, Long> calculateStatusCounts(List<OrderPreviewResponse> orders) {
        // 실제 주문 상태 카운트 (DeliveryStatus 기준)
        Map<String, Long> counts = orders.stream()
                .map(order -> order.deliveryStatus().name())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 교환/반품 요청 카운트 집계 (OrderItem ConfirmStatus 기준)
        long refundRequestCount = orders.stream()
                .flatMap(order -> order.orderItems().stream()) // 주문 항목 리스트를 스트림으로 펼치기
                .filter(item -> {
                    ConfirmStatus status = item.confirmStatus();
                    // REFUND_REQUEST (반품 요청) 상태만 카운트
                    return status != null && status == ConfirmStatus.REFUND_REQUEST;
                })
                .count();

        // 기본값 설정 (뷰에서 null 방지)
        counts.putIfAbsent("WAITING", 0L);
        counts.putIfAbsent("SHIPPING", 0L);
        counts.putIfAbsent("DELIVERED", 0L);
        counts.putIfAbsent("CANCELED", 0L);

        // UI 전용 필드 업데이트
        counts.put("CANCELED", counts.get("CANCELED"));
        counts.put("REFUND_REQUEST", refundRequestCount);
        return counts;
    }

    // 선택된 상태에 따라 필터링 (수정됨)
    private List<OrderPreviewResponse> filterOrdersByStatus(List<OrderPreviewResponse> allOrders,
                                                            String status) {
        if ("ALL".equalsIgnoreCase(status)) {
            return allOrders;
        }

        String filter = status.toUpperCase();

        // 교환/반품 요청 상태 필터링 (ConfirmStatus 기준)
        if ("REFUND_REQUEST".equals(filter)) {
            return allOrders.stream()
                    .filter(order -> order.orderItems().stream().anyMatch(item -> {
                        ConfirmStatus itemStatus = item.confirmStatus();
                        // REFUND_REQUEST 상태만 필터링
                        return itemStatus != null && itemStatus == ConfirmStatus.REFUND_REQUEST;
                    }))
                    .collect(Collectors.toList());
        }

        // 취소 주문 필터링
        if ("CANCELED".equals(filter)) {
            filter = "CANCELED";
        }

        String finalFilter = filter;

        // DeliveryStatus 기준 필터링
        return allOrders.stream()
                .filter(order -> order.deliveryStatus().name().equalsIgnoreCase(finalFilter))
                .collect(Collectors.toList());
    }

    // UI용 한글 상태명 (수정됨)
    private Map<String, String> getStatusKoreanNameMap() {
        return Map.of(
                "WAITING", "배송 준비중",
                "SHIPPING", "배송 중",
                "DELIVERED", "배송 완료",
                "CANCELED", "취소됨",
                "REFUND_REQUEST", "반품 요청"
        );
    }

    /**
     * 주문에 대한 전체 주문아이템 구매확정 버튼(마이페이지)
     */
    @PostMapping("/confirm-items")
    @ResponseBody
    public Map<String, Object> confirmOrder(@RequestParam(value = "orderId") Long orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 백엔드에 -> order id를 보내 ->
            orderItemApiClient.confirmOrder(orderId);

            response.put("success", true);
            response.put("message", "주문 항목 일괄 구매 확정 성공 (주문 ID: " + orderId + ")");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "주문 항목 일괄 구매 확정 처리 중 오류 발생: " + e.getMessage());
        }
        return response;
    }
}