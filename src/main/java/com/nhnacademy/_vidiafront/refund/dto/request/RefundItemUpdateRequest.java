package com.nhnacademy._vidiafront.refund.dto.request;

import com.nhnacademy._vidiafront.refund.dto.response.RefundItemStatus;

public record RefundItemUpdateRequest (
        RefundItemStatus refundItemStatus,
        String rejectDetail // 거절에만
) {
}
