package com.nhnacademy._vidiafront.order.config;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentCancelRequest;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {
    private final RestClient restClient;
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    // 결제 확정 및 결제 저장
    public void confirmPayment(PaymentConfirmRequest confirmRequest, long id) {
        backendApiClient.post(ORDER_SERVICE + "/orders/" + id + "/success", confirmRequest, void.class);
    }

    // 결제 취소인데 배송 전 전체 취소만 해당 - 출고일 이후는 포인트로 돌려줌
    public void cancelPayment(PaymentCancelRequest cancelRequest, long orderId) {
        backendApiClient.post(ORDER_SERVICE + "/orders/" + orderId + "/cancel", cancelRequest, void.class);
    }
}