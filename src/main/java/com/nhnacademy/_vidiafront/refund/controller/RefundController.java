package com.nhnacademy._vidiafront.refund.controller;

import com.nhnacademy._vidiafront.refund.client.RefundApiClient;
import com.nhnacademy._vidiafront.refund.dto.request.RefundRequest;
import com.nhnacademy._vidiafront.refund.dto.response.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RefundController {
    private final RefundApiClient refundApiClient;
    /**
     * 반품 신청 페이지 열기
     */
    @GetMapping("/orders/{orderId}/refund")
    public String showRefundForm(@PathVariable Long orderId,
                                 Model model){
        RefundResponse refundResponse = refundApiClient.getRefundList(orderId);
        model.addAttribute("order", refundResponse);
        return "mypage/order/refundPage";
    }

    @PostMapping("/refunds")
    public String refundRegister(RefundRequest refundRequest){
        refundApiClient.refundRegister(refundRequest);
        return "redirect:/mypage/order?status=DELIVERED";
    }
}