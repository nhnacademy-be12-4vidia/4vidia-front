package com.nhnacademy._vidiafront.order.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    //주문아이템 확정
    public void confirmOrderItem(long orderItemId) {
        backendApiClient.post(ORDER_SERVICE + "/orders/confirm-item", orderItemId, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public void confirmOrder(long orderId) {
        backendApiClient.putNoBody(ORDER_SERVICE + "/orders/" + orderId + "/confirm-order", new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }
}
