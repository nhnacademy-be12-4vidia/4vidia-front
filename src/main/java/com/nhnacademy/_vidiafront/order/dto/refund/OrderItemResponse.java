package com.nhnacademy._vidiafront.order.dto.refund;

public record OrderItemResponse(
        Long orderItemId,
        Long bookId,
        String bookTitle,
        Integer quantity
){}
