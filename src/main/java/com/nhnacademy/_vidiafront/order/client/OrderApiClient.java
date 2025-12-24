package com.nhnacademy._vidiafront.order.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutListRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderTrackingRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    // 주문아이템 Redis에 임시 생성
    public String saveTempOrderCheckout(List<OrderCheckoutRequest> orderCheckoutRequests) {
        OrderCheckoutListRequest orderCheckoutListRequest = new OrderCheckoutListRequest(orderCheckoutRequests);
        String orderKey = backendApiClient.post(ORDER_SERVICE + "/orders/checkout-temp", orderCheckoutListRequest, String.class);
        return orderKey;
    }

    // 주문 화면에 필요한 정보 가져오기
    public OrderCheckoutResponse getOrderCheckout(String orderKey) {
        OrderCheckoutResponse orderCheckoutResponse = backendApiClient.get(ORDER_SERVICE + "/orders?key=" + orderKey, OrderCheckoutResponse.class);
        return orderCheckoutResponse;
    }

    // 주문 생성
    public OrderCreateResponse saveOrder(@Valid OrderCreateRequest orderCreateRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/orders", orderCreateRequest, OrderCreateResponse.class);
    }

    //Order 한개 내역 (연결된 오더아이템도)가져오기
    public OrderResponse getOrderById(long orderId) {
        OrderResponse orderResponse = backendApiClient.get(ORDER_SERVICE + "/orders/" + orderId, OrderResponse.class);
        return orderResponse;
    }

    //주문내역 미리보기
    public PageResponse<OrderPreviewResponse> getOrderPreview(int page, int size, String status) { // 기존 "/my/orders"
        ParameterizedTypeReference<PageResponse<OrderPreviewResponse>> typeReference =
                new ParameterizedTypeReference<PageResponse<OrderPreviewResponse>>() {};

        String url = ORDER_SERVICE + "/users/me/orders?page=" + page + "&size=" + size + "&status=" + status;

        return backendApiClient.get(url, typeReference);
    }

    // 주문 상태별 카운트 조회
    // 탭 상단에 표시될 숫자(전체, 준비중, 배송중 등)를 가져옴
    public OrderCountResponse getOrderCounts() {
        return backendApiClient.get(ORDER_SERVICE + "/users/me/orders/counts", OrderCountResponse.class);
    }

    public void cancelOrder(long orderId) {
        backendApiClient.putNoBody(ORDER_SERVICE + "/orders/" + orderId + "/cancel", Void.class);
    }

    public OrderResponse getGuestOrder(OrderTrackingRequest orderTrackingRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/orders/guest", orderTrackingRequest, OrderResponse.class);
    }
}
