package com.nhnacademy._vidiafront.order.config;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCheckoutResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCreateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderApiClient {
    private final RestClient restClient;
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    //주문아이템 정보 넘기고 주문 화면에 필요한 정보 가져오기
    public OrderCheckoutResponse getOrderCheckout(List<OrderCheckoutRequest> orderCheckoutRequests) {
        OrderCheckoutResponse orderCheckoutResponse = backendApiClient.post(ORDER_SERVICE + "/orders", orderCheckoutRequests, OrderCheckoutResponse.class);
        return orderCheckoutResponse;
    }

    //Order 한개 내역 (연결된 오더아이템도)가져오기
    public OrderResponse getOrderById(long orderId) {
        OrderResponse orderResponse = backendApiClient.get(ORDER_SERVICE + "/orders/" + orderId, OrderResponse.class);
        return orderResponse;
    }

    //주문내역 미리보기
    public List<OrderPreviewResponse> getOrderPreview() {
        ParameterizedTypeReference<List<OrderPreviewResponse>> typeReference =
                new ParameterizedTypeReference<List<OrderPreviewResponse>>() {};

        List<OrderPreviewResponse> orderPreviewResponses = backendApiClient.get(ORDER_SERVICE + "/orders", typeReference);
        return orderPreviewResponses;
    }

    public OrderCreateResponse saveOrder(OrderCreateRequest orderCreateRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/orders", orderCreateRequest, OrderCreateResponse.class);
    }
}
