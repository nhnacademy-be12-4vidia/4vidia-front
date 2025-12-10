package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminDeliveryApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminDeliverySearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminDeliveryPageResponse;
import com.nhnacademy._vidiafront.admin.dto.response.DeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {

    private final AdminDeliveryApiClient deliveryApiClient;

    /**
     * 관리자 배송 관리 페이지
     */
    @GetMapping
    public String deliveryList(
            @RequestParam(required = false) String deliveryStatus,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            Model model
    ) {
        AdminDeliverySearchRequest request = new AdminDeliverySearchRequest(deliveryStatus, page, size);
        AdminDeliveryPageResponse pageResponse = deliveryApiClient.getOrderPage(request);

        model.addAttribute("page", pageResponse);
        model.addAttribute("currentDelivery", deliveryStatus);

        return "admin/admin-delivery-list";
    }

    /**
     * 배송 상세 페이지
     */
    @GetMapping("/{orderId}")
    public String detail(
            @PathVariable Long orderId,
            Model model
    ) {
        DeliveryResponse order = deliveryApiClient.getOrder(orderId);

        model.addAttribute("order", order);
        return "admin/admin-delivery-detail";
    }

    /**
     * 배송 시작 처리 (폼 POST)
     */
    @PostMapping("/{orderId}/start")
    public String startDelivery(
            @PathVariable Long orderId,
            @RequestParam(required = false, defaultValue = "WAITING") String deliveryStatus,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        deliveryApiClient.startDelivery(orderId);

        String redirect = UriComponentsBuilder.fromPath("/admin/delivery")
                .queryParam("deliveryStatus", "SHIPPING")
                .queryParam("page", page)
                .toUriString();

        return "redirect:" + redirect;
    }

    /**
     * 배송 완료 처리 (폼 POST)
     */
    @PostMapping("/{orderId}/complete")
    public String completeDelivery(
            @PathVariable Long orderId,
            @RequestParam(required = false, defaultValue = "SHIPPING") String deliveryStatus,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        deliveryApiClient.completeDelivery(orderId);

        String redirect = UriComponentsBuilder.fromPath("/admin/delivery")
                .queryParam("deliveryStatus", "DELIVERED")
                .queryParam("page", page)
                .toUriString();

        return "redirect:" + redirect;
    }
}
