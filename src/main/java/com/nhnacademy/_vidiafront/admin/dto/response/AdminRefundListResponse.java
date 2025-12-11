package com.nhnacademy._vidiafront.admin.dto.response;

import java.time.LocalDateTime;

public record AdminRefundListResponse(
        Long refundId,
        Long orderId,
        String email,
        String name,
        LocalDateTime createdAt,
        String refundStatus
) {
}
