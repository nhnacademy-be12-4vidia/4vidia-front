package com.nhnacademy._vidiafront.cart.dto.response;

public record BookSummaryResponse(
        Long id,
        String title,
        int priceStandard,
        int priceSales,
        String imageUrl
) {
}

