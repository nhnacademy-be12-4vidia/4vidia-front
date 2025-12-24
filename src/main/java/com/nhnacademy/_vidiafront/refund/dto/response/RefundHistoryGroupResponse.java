package com.nhnacademy._vidiafront.refund.dto.response;

import com.nhnacademy._vidiafront.refund.dto.RefundStatus;

import java.time.LocalDate;
import java.util.List;

public record RefundHistoryGroupResponse(
        Long refundId,
        Long orderId,
        LocalDate orderDate,
        LocalDate refundDate,
        RefundStatus refundStatus,
        String refundReason,
        Integer totalRefundPrice,

        List<RefundItemResponse> items
){
    public record RefundItemResponse(
            String title,
            int quantity,
            int price,
            RefundItemStatus refundItemStatus,
            String rejectDetail // 거절 사유 (아이템별)

    ) {}
}