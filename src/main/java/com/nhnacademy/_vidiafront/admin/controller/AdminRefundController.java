package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminRefundApiClient;
import com.nhnacademy._vidiafront.admin.dto.response.AdminRefundListResponse;
import com.nhnacademy._vidiafront.admin.dto.response.RefundDetailResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/refunds")
public class AdminRefundController {
    private final AdminRefundApiClient adminRefundApiClient;

    /**
     * 관리자 반품 조회
     */
    @GetMapping
    public String getRefundList(@RequestParam(required = false, defaultValue = "PROCESS") String refundStatus,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                Model model
    ){
        PageResponse<AdminRefundListResponse> pageResponse = adminRefundApiClient.getRefundList(refundStatus, keyword, page, size);

        model.addAttribute("refundsPage", pageResponse);
        model.addAttribute("status", refundStatus);
        model.addAttribute("keyword", keyword);

        return "admin/admin-refund-list";
    }

    /**
     * 반품 상세 조회
     */
    @GetMapping("/{refundId}")
    @ResponseBody
    public ResponseEntity<RefundDetailResponse> getRefundDetail(@PathVariable Long refundId) {
        RefundDetailResponse response = adminRefundApiClient.getRefundDetail(refundId);
        return ResponseEntity.ok(response);
    }

    // 승인
    @PostMapping("/{id}/accept")
    @ResponseBody
    public ResponseEntity<Void> acceptRefund(@PathVariable Long id) {
        adminRefundApiClient.acceptRefund(id); // client에 POST 위임
        return ResponseEntity.ok().build();
    }

    // 거절
    @PostMapping("/{id}/reject")
    @ResponseBody
    public ResponseEntity<Void> rejectRefund(@PathVariable Long id) {
        adminRefundApiClient.rejectRefund(id); // client에 POST 위임
        return ResponseEntity.ok().build();
    }

}
