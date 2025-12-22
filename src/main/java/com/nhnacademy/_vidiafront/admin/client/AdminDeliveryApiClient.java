package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminDeliverySearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminDeliveryPageResponse;
import com.nhnacademy._vidiafront.admin.dto.response.DeliveryResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AdminDeliveryApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";

    private final BackendApiClient backendApiClient;

    /**
     * 관리자 주문(배송) 목록 조회 (페이징 + 상태)
     */
    public AdminDeliveryPageResponse getOrderPage(AdminDeliverySearchRequest request) {
        var builder = UriComponentsBuilder
                .fromPath(ORDER_SERVICE + "/admin/deliveries")
                .queryParam("page", request.pageOrDefault())
                .queryParam("size", request.sizeOrDefault());

        String deliveryStatus = request.deliveryStatusOrNull();
        if(deliveryStatus != null){
            builder.queryParam("deliveryStatus", deliveryStatus);
        }


        String keyword = request.keywordOrNull();
        if (keyword != null) {
            builder.queryParam("keyword", keyword);
        }

        String url = builder.toUriString();
        return backendApiClient.get(url, new ParameterizedTypeReference<>(){});
    }

    /**
     * 단일 주문 상세 조회
     */
    public DeliveryResponse getOrder(Long orderId) {
        String url = ORDER_SERVICE + "/admin/deliveries/" + orderId;
        return backendApiClient.get(url, DeliveryResponse.class);
    }

    /**
     * 배송 시작
     */
    public void startDelivery(Long orderId) {
        String url = ORDER_SERVICE + "/admin/deliveries/" + orderId + "/start-delivery";
        backendApiClient.putNoBody(url, Void.class);
    }

    /**
     * 배송 완료
     */
    public void completeDelivery(Long orderId) {
        String url = ORDER_SERVICE + "/admin/deliveries/" + orderId + "/complete-delivery";
        backendApiClient.putNoBody(url, Void.class);
    }
}
