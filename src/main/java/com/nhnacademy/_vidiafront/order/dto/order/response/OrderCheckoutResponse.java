package com.nhnacademy._vidiafront.order.dto.order.response;

import java.util.List;

public record OrderCheckoutResponse(
        String orderName,
        int finalAmount,
        List<OrderBookResponse> bookItems,
        List<DeliveryDateResponse> deliveryDateResponses,
        List<PackagingOptionResponse> packagingOptions
) {
    public record DeliveryDateResponse(
            String value,
            String displayDate
    ) { }

    public record PackagingOptionResponse(
            long packagingOptionId,
            String name,
            int price
    ) { }
}