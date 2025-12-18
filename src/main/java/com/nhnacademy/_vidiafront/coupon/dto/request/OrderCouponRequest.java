package com.nhnacademy._vidiafront.coupon.dto.request;

import java.util.List;

public record OrderCouponRequest(
        List<OrderBookResponse> orderBookResponses
) {
    public record OrderBookResponse(
            Long bookId,
            String categoryKdc,
            Integer quantity,
            Integer salePrice
    ) {
        public static OrderBookResponse from(com.nhnacademy._vidiafront.order.dto.order.response.OrderBookResponse orderItem) {
            return new OrderBookResponse(
                    orderItem.bookId(),
                    orderItem.categoryKdc(),
                    orderItem.quantity(),
                    orderItem.salePrice()
            );
        }
    }

    public static OrderCouponRequest from(List<com.nhnacademy._vidiafront.order.dto.order.response.OrderBookResponse> orderItem) {
        List<OrderBookResponse> orderBookResponses = orderItem.stream()
                .map(OrderBookResponse::from)
                .toList();

        return new OrderCouponRequest(orderBookResponses);
    }
}