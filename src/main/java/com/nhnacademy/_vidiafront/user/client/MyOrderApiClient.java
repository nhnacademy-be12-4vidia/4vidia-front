package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.user.response.OrderUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyOrderApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String BASE_URL = "/users/me";

    /**
     * 주문에 필요한 회원 정보 조회
     */
    public OrderUserResponse getUserOrderInfo() {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/order-info", new ParameterizedTypeReference<>() {});
    }
}
