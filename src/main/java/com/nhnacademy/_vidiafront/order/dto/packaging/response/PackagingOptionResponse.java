package com.nhnacademy._vidiafront.order.dto.packaging.response;

public record PackagingOptionResponse(
        long packagingOptionId,
        String name,
        int price
) {
}
