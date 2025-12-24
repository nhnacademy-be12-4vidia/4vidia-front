package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.global.dto.PageResponse;
import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.client.OrderItemApiClient;
import com.nhnacademy._vidiafront.order.dto.ConfirmStatus;
import com.nhnacademy._vidiafront.order.dto.OrderItemViewStatus;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCountResponse;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        PageResponse<OrderPreviewResponse> orderResponse = getOrderList(page, size, status);

        OrderCountResponse countResponse = getOrderCounts();

        model.addAttribute("currentStatus", status.toUpperCase());
        model.addAttribute("orders", orderResponse.content());      // 현재 페이지의 주문 목록
        model.addAttribute("pageResponse", orderResponse);          // 페이징 정보 (totalPages 등)
        model.addAttribute("totalOrders", orderResponse.totalElements()); // 현재 상태의 총 주문 수
        model.addAttribute("counts", countResponse);                // 탭 상단에 표시할 상태별 카운트

        return "mypage/order/orderList";
    }

    private PageResponse<OrderPreviewResponse> getOrderList(int page, int size, String status) {
        try {
            return orderApiClient.getOrderPreview(page, size, status);
        } catch (Exception e) {
            log.error("Failed to fetch order preview list: {}", e.getMessage());
            // 에러 시 빈 객체 반환 (Record 생성자 사용)
            return new PageResponse<>(Collections.emptyList(), page, size, 0, 0, true);
        }
    }

    private OrderCountResponse getOrderCounts() {
        try {
            return orderApiClient.getOrderCounts();
        } catch (Exception e) {
            log.error("Failed to fetch order counts: {}", e.getMessage());
            // 에러 발생 시 0으로 채워진 객체 반환 (화면 깨짐 방지)
            return new OrderCountResponse(0, 0, 0, 0, 0);
        }
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