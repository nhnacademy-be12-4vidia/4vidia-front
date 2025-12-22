package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminRefundApiClient;
import com.nhnacademy._vidiafront.admin.dto.response.AdminRefundListResponse;
import com.nhnacademy._vidiafront.admin.dto.response.RefundDetailResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import com.nhnacademy._vidiafront.refund.client.RefundApiClient;
import com.nhnacademy._vidiafront.refund.dto.RefundStatus;
import com.nhnacademy._vidiafront.refund.dto.request.RefundItemUpdateRequest;
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
    private final RefundApiClient refundApiClient;

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
//    @GetMapping("/{refundId}")
//    @ResponseBody
//    public ResponseEntity<RefundDetailResponse> getRefundDetail(@PathVariable Long refundId) {
//        RefundDetailResponse response = adminRefundApiClient.getRefundDetail(refundId);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{refundId}")
    public String detail(@PathVariable Long refundId,
                         @RequestParam(required = false, defaultValue = "0") Integer page,
                         @RequestParam(required = false) String status,
                         Model model){
        RefundDetailResponse refund = adminRefundApiClient.getRefundDetail(refundId);

        model.addAttribute("refund", refund);
        model.addAttribute("page", page);
        model.addAttribute("currentRefund", status);

        return "admin/admin-refund-detail";
    }


    // 반품 승인/거절
    @PostMapping("/admin/refunds/{refundItemId}")
    public String updateRefund(@PathVariable Long refundItemId,
                               @RequestParam RefundStatus refundStatus,
                               @RequestParam(required = false) String rejectDetail,
                               @RequestParam int page) {

        RefundItemUpdateRequest request =
                new RefundItemUpdateRequest(
                        refundStatus,
                        rejectDetail
                );

        adminRefundApiClient.updateRefund(refundItemId, request);

        return "redirect:/admin/refunds?page=" + page;
    }

}
