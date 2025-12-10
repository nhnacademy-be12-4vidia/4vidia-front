package com.nhnacademy._vidiafront.order.dto.order.request;

public record OrderTrackingRequest(
        Long orderId,
        String orderPassword
) {
}
