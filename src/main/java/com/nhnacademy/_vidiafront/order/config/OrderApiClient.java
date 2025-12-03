package com.nhnacademy._vidiafront.order.config;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.DeliveryDateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCreateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderPreviewResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
import com.nhnacademy._vidiafront.order.dto.packaging.response.PackagingOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderApiClient {
    private final RestClient restClient;
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    private static final String TEST_ID = "1";

    // PackagingOption 종류 가져오기
    public List<PackagingOptionResponse> getPackagingOptions() {
        ParameterizedTypeReference<List<PackagingOptionResponse>> typeReference =
                new ParameterizedTypeReference<List<PackagingOptionResponse>>() {};
        List<PackagingOptionResponse> packagingOptionResponses = backendApiClient.get(ORDER_SERVICE + "/packaging-options", typeReference);
        return packagingOptionResponses;
    }

    //Order 한개 내역 (연결된 오더아이템도)가져오기
    public OrderResponse getOrderById(long orderId) {
        OrderResponse orderResponse = backendApiClient.get(ORDER_SERVICE + "/orders/{orderId}".formatted(orderId), OrderResponse.class);
        return orderResponse;
    }

    // 배송가능일 가져오기
    public List<DeliveryDateResponse> getDeliveryDates() {
        ParameterizedTypeReference<List<DeliveryDateResponse>> typeReference =
                new ParameterizedTypeReference<List<DeliveryDateResponse>>() {};

        List<DeliveryDateResponse> deliveryDateResponses = backendApiClient.get(ORDER_SERVICE + "/delivery-dates", typeReference);
        return deliveryDateResponses;
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
