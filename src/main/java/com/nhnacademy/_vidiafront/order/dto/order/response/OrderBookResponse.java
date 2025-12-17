package com.nhnacademy._vidiafront.order.dto.order.response;

public record OrderBookResponse(
            Long bookId,
            String bookTitle,
            String bookAuthor,
            String bookImageUrl,
            String categoryKdc,
            Integer quantity,
            Integer salePrice
) { }