package com.nhnacademy._vidiafront.order.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    //주문아이템 확정
    public void confirmOrderItem(long orderItemId) {
        backendApiClient.post(ORDER_SERVICE + "/orders/confirm-item", orderItemId, Void.class);
    }

    public void confirmOrder(long orderId) {
        backendApiClient.put(ORDER_SERVICE + "/orders/confirm-order", orderId, Void.class);
    }
}
