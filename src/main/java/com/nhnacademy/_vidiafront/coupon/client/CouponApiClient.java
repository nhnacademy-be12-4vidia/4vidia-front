package com.nhnacademy._vidiafront.coupon.client;

import com.nhnacademy._vidiafront.coupon.dto.OrderCouponRequest;
import com.nhnacademy._vidiafront.coupon.dto.OrderCouponResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.order.dto.order.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponApiClient {
    private static final String COUPON_SERVICE = "/api/v1/coupon-service";
    private final BackendApiClient backendApiClient;

    public OrderCouponResponse orderCouponResponse(List<OrderBookResponse> bookItems) {
        OrderCouponRequest orderCouponRequest = OrderCouponRequest.from(bookItems);
        OrderCouponResponse OrderCouponResponse = backendApiClient.post(COUPON_SERVICE + "/coupons/validate", orderCouponRequest, OrderCouponResponse.class);
        return OrderCouponResponse;
    }
}
