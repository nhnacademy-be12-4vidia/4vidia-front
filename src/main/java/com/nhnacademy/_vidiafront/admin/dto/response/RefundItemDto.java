package com.nhnacademy._vidiafront.admin.dto.response;

public record RefundItemDto(
        Long orderItemId,
        String bookTitle,
        int quantity,
        long price
) {}
