package com.nhnacademy._vidiafront.order.dto.order.request;

public record OrderCheckoutRequest(
        Long bookId,
        int quantity
) {
    public static OrderCheckoutRequest from(Long bookId, int quantity) {
        return new OrderCheckoutRequest(
                bookId, quantity
        );
    }
}
