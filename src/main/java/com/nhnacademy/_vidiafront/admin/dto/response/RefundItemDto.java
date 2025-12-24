package com.nhnacademy._vidiafront.admin.dto.response;

import com.nhnacademy._vidiafront.refund.dto.response.RefundItemStatus;

public record RefundItemDto(
        Long refundItemId,
        RefundItemStatus refundItemStatus,
        String bookTitle,
        String bookImgUrl,
        int quantity,
        long salePrice
) {
}

