package com.nhnacademy._vidiafront.order.config;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentCancelRequest;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    // 결제 확정 및 결제 저장
    public PaymentResponse confirmPayment(PaymentConfirmRequest confirmRequest, long id) { //주문과정 4번
        return backendApiClient.post(ORDER_SERVICE + "/orders/" + id + "/success", confirmRequest, PaymentResponse.class);
    }

    // 결제 취소인데 배송 전 전체 취소만 해당 - 출고일 이후는 포인트로 돌려줌
    public PaymentResponse cancelPayment(PaymentCancelRequest cancelRequest, long orderId) {
        return backendApiClient.post(ORDER_SERVICE + "/orders/" + orderId + "/cancel", cancelRequest, PaymentResponse.class);
    }

    //결제 조회
    public PaymentResponse getPayment(long orderId) {
        return backendApiClient.get(ORDER_SERVICE + "/orders/pay/" + orderId, PaymentResponse.class);
    }
}