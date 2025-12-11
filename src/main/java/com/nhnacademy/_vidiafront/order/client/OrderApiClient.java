package com.nhnacademy._vidiafront.order.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutListRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderTrackingRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCheckoutResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCreateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
import com.nhnacademy._vidiafront.order.dto.refund.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    //주문아이템 정보 넘기고 주문 화면에 필요한 정보 가져오기
    public OrderCheckoutResponse getOrderCheckout(List<OrderCheckoutRequest> orderCheckoutRequests) {
        OrderCheckoutListRequest orderCheckoutListRequest = new OrderCheckoutListRequest(orderCheckoutRequests);
        OrderCheckoutResponse orderCheckoutResponse = backendApiClient.post(ORDER_SERVICE + "/orders", orderCheckoutListRequest, OrderCheckoutResponse.class);
        return orderCheckoutResponse;
    }

    //Order 한개 내역 (연결된 오더아이템도)가져오기
    public OrderResponse getOrderById(long orderId) {
        OrderResponse orderResponse = backendApiClient.get(ORDER_SERVICE + "/orders/" + orderId, OrderResponse.class);
        return orderResponse;
    }

    //주문내역 미리보기
    public List<OrderPreviewResponse> getOrderPreview() { // 기존 "/my/orders"
        ParameterizedTypeReference<List<OrderPreviewResponse>> typeReference =
                new ParameterizedTypeReference<List<OrderPreviewResponse>>() {};

        List<OrderPreviewResponse> orderPreviewResponses = backendApiClient.get(ORDER_SERVICE + "/users/me/orders", typeReference);
        return orderPreviewResponses;
    }

    public OrderCreateResponse saveOrder(OrderCreateRequest orderCreateRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/orders/create", orderCreateRequest, OrderCreateResponse.class);
    }

    public void cancelOrder(long orderId) {
        backendApiClient.putNoBody(ORDER_SERVICE + "/orders/" + orderId + "/cancel", Void.class);
    }

    public OrderResponse getGuestOrder(OrderTrackingRequest orderTrackingRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/orders/guest", orderTrackingRequest, OrderResponse.class);
    }

    /**
     * 반품
     */
    public RefundResponse getRefundList(long orderId){
        return backendApiClient.get(ORDER_SERVICE + "/orders/" + orderId + "/refunds" , RefundResponse.class);
    }
}
