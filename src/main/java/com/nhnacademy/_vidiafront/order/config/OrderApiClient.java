package com.nhnacademy._vidiafront.order.config;

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

    private static final String TEST_ID = "1";

    // PackagingOption 종류 가져오기
    public List<PackagingOptionResponse> getPackagingOptions() {
        ParameterizedTypeReference<List<PackagingOptionResponse>> typeReference =
                new ParameterizedTypeReference<List<PackagingOptionResponse>>() {};

        List<PackagingOptionResponse> packagingOptions;
        try {
            packagingOptions = restClient.get()
                    .uri(ORDER_SERVICE + "/packaging-options")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .retrieve()
                    .body(typeReference);
        } catch (RestClientException e) {
            throw new ApiRequestException("api PackagingOption 가져오기 실패" + e.getMessage());
        }
        return packagingOptions;
    }

    //Order 한개 내역 (연결된 오더아이템도)가져오기
    public OrderResponse getOrderById(long orderId) {

        OrderResponse order;
        try {
            order = restClient.get()
                    .uri(ORDER_SERVICE + "/orders/{orderId}", orderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .retrieve()
                    .body(OrderResponse.class);
        } catch (RestClientException e) {
            throw new ApiRequestException("api Order 가져오기 실패" + e.getMessage());
        }
        return order;
    }

    // 배송가능일 가져오기
    public List<DeliveryDateResponse> getDeliveryDates() {
        ParameterizedTypeReference<List<DeliveryDateResponse>> typeReference =
                new ParameterizedTypeReference<List<DeliveryDateResponse>>() {};

        List<DeliveryDateResponse> deliveryDates;
        try {
            deliveryDates = restClient.get()
                    .uri(ORDER_SERVICE + "/orders/delivery-dates")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .retrieve()
                    .body(typeReference);
        } catch (RestClientException e) {
            throw new ApiRequestException("api DeliveryDate 가져오기 실패" + e.getMessage());
        }
        return deliveryDates;
    }

    //주문내역 미리보기
    public List<OrderPreviewResponse> getOrderPreview() {
        ParameterizedTypeReference<List<OrderPreviewResponse>> typeReference =
                new ParameterizedTypeReference<List<OrderPreviewResponse>>() {};

        List<OrderPreviewResponse> orderPreviewResponses;
        try {
            orderPreviewResponses = restClient.get()
                    .uri(ORDER_SERVICE + "/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .retrieve()
                    .body(typeReference);
        } catch (RestClientException e) {
            throw new ApiRequestException("api OrderPreviewResponse 가져오기 실패" + e.getMessage());
        }
        return orderPreviewResponses;
    }


    public OrderCreateResponse saveOrder(OrderCreateRequest orderCreateRequest) {

        OrderCreateResponse orderId;
        try {
            orderId = restClient.post()
                    .uri(ORDER_SERVICE + "/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .body(orderCreateRequest)
                    .retrieve()
                    .body(OrderCreateResponse.class);
        } catch (RestClientException e) {
            throw new ApiRequestException("api Order 저장 실패" + e.getMessage());
        }
        return orderId;
    }





}
