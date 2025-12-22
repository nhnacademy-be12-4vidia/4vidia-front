package com.nhnacademy._vidiafront.refund.dto.request;

import com.nhnacademy._vidiafront.refund.dto.RefundStatus;

public record RefundItemUpdateRequest (
        RefundStatus refundStatus,
        String rejectDetail // 거절에만
) {
}
