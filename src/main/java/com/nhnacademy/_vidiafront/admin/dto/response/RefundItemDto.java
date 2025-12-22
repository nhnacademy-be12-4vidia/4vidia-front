package com.nhnacademy._vidiafront.admin.dto.response;

public record RefundItemDto(
        Long refundItemId,
        String bookTitle,
        String bookImgUrl,
        int quantity,
        long salePrice
) {
}

