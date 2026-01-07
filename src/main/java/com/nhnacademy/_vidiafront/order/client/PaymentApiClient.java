package com.nhnacademy._vidiafront.order.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentFailRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    // 결제 확정 및 결제 저장
    public PaymentResponse confirmPayment(PaymentConfirmRequest confirmRequest, long id) {
        return backendApiClient.post(ORDER_SERVICE + "/payments?id=" + id, confirmRequest, new ParameterizedTypeReference<>() {});
    }

    // 결제 성공시 한개 조회
    public PaymentResponse getPayment(long orderId) {
        return backendApiClient.get(ORDER_SERVICE + "/payments/" + orderId, new ParameterizedTypeReference<>() {});
    }

    // 결제창 실패시 롤백 요청
    public Void rollbackPayment(PaymentFailRequest paymentFailRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/payments/rollback", paymentFailRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

}