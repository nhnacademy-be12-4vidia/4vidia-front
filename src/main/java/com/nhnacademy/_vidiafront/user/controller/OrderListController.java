package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.client.OrderItemApiClient;
import com.nhnacademy._vidiafront.order.dto.ConfirmStatus;
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

    // 상태 카운트 계산 + 반드시 기본값 세팅 (수정됨)
    private Map<String, Long> calculateStatusCounts(List<OrderPreviewResponse> orders) {
        // 실제 주문 상태 카운트 (DeliveryStatus 기준)
        Map<String, Long> counts = orders.stream()
                .map(order -> order.deliveryStatus().name())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 💡 교환/반품 요청 카운트 집계 (OrderItem ConfirmStatus 기준)
        long exchangeReturnCount = orders.stream()
                .flatMap(order -> order.orderItems().stream()) // 주문 항목 리스트를 스트림으로 펼치기
                .filter(item -> {
                    ConfirmStatus status = item.confirmStatus();
                    // REFUND_REQUEST (반품 요청) 또는 EXCHANGE_REQUEST (교환 요청) 상태를 카운트
                    return status != null &&
                            (status.name().equals("REFUND_REQUEST") || status.name().equals("EXCHANGE_REQUEST"));
                })
                .count();

        // 기본값 설정 (뷰에서 null 방지)
        counts.putIfAbsent("WAITING", 0L);
        counts.putIfAbsent("SHIPPING", 0L);
        counts.putIfAbsent("DELIVERED", 0L);
        counts.putIfAbsent("CANCELED", 0L);

        // UI 전용 필드 업데이트
        counts.put("CANCELED_ONLY", counts.get("CANCELED"));
        counts.put("EXCHANGE_RETURN", exchangeReturnCount); // 집계된 교환/반품 요청 카운트 반영
        return counts;
    }

    // 선택된 상태에 따라 필터링 (수정됨)
    private List<OrderPreviewResponse> filterOrdersByStatus(List<OrderPreviewResponse> allOrders,
                                                            String status) {
        if ("ALL".equalsIgnoreCase(status)) {
            return allOrders;
        }

        String filter = status.toUpperCase();

        // 💡 교환/반품 요청 상태 필터링 (ConfirmStatus 기준)
        if ("EXCHANGE_RETURN".equals(filter)) {
            return allOrders.stream()
                    .filter(order -> order.orderItems().stream().anyMatch(item -> {
                        ConfirmStatus itemStatus = item.confirmStatus();
                        return itemStatus != null &&
                                (itemStatus.name().equals("REFUND_REQUEST") || itemStatus.name().equals("EXCHANGE_REQUEST"));
                    }))
                    .collect(Collectors.toList());
        }

        // 취소 주문 필터링
        if ("CANCELED_ONLY".equals(filter)) {
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
                "EXCHANGE_RETURN", "교환/반품 요청"
        );
    }

    /**
     * 개별 구매 확정버튼(마이페이지)
     */
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


    /**
     * 일괄 구매 확정버튼(마이페이지)
     */
    @PostMapping("/confirm-items")
    @ResponseBody
    public Map<String, Object> confirmOrder(@RequestParam(value = "orderId") Long orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 해당 orderId에 속한 모든 UNCONFIRMED orderItems를 CONFIRMED로 변경
            // todo : 반품 신청한거 제외해야함 -> 현재 로직은 API 호출 전에 필터링을 하지 않고 있어 불필요한 반복이 발생할 수 있음.
            //        클라이언트(JS)에서 이미 필터링된 항목만 보낸다고 가정하거나, 서버에서 orderId로 필터링해야 함.
            //        현재 코드는 orderId를 사용하지 않고 전체 주문을 순회하고 있습니다. orderId에 해당하는 주문만 처리하도록 수정해야 안전합니다.

            // 임시 수정 (orderId를 사용하도록)
            List<OrderPreviewResponse> orders = orderApiClient.getOrderPreview();
            for (OrderPreviewResponse order : orders) {
                if (order.orderId() == orderId) { // orderId로 주문 필터링
                    for (OrderPreviewResponse.OrderBookResponse item : order.orderItems()) {
                        // UNCONFIRMED 상태의 항목만 확정 처리 (반품 요청된 항목은 제외)
                        if (item.confirmStatus().name().equals("UNCONFIRMED")) {
                            orderItemApiClient.confirmOrderItem(item.orderItemId());
                        }
                    }
                    break; // 해당 주문을 찾았으면 루프 종료
                }
            }

            response.put("success", true);
            response.put("message", "주문 항목 일괄 구매 확정 성공 (주문 ID: " + orderId + ")");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "주문 항목 일괄 구매 확정 처리 중 오류 발생: " + e.getMessage());
        }
        return response;
    }
}