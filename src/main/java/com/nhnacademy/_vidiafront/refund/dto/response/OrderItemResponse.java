package com.nhnacademy._vidiafront.refund.dto.response;

public record OrderItemResponse(
        Long orderItemId,
        Long bookId,
        String bookTitle,
        Integer quantity
){}
