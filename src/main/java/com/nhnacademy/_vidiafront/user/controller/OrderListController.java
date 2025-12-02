package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.order.config.OrderApiClient;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage/order")
@Controller
public class OrderListController {
    private final OrderApiClient orderApiClient;

    /**
     * 주문관리 페이지 폼
     * */
    @GetMapping
    public String orderList(Model model) {
        List<OrderPreviewResponse> orders = orderApiClient.getOrderPreview();
        model.addAttribute("orders", orders);


        return "mypage/order/orderList";
    }

}
