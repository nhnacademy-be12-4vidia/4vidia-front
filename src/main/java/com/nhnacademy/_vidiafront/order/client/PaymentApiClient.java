package com.nhnacademy._vidiafront.order.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentFailRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    // 결제 확정 및 결제 저장
    public PaymentResponse confirmPayment(PaymentConfirmRequest confirmRequest, long id) {
        return backendApiClient.post(ORDER_SERVICE + "/payments?id=" + id, confirmRequest, PaymentResponse.class);
    }

    // 결제 성공시 한개 조회
    public PaymentResponse getPayment(long orderId) {
        return backendApiClient.get(ORDER_SERVICE + "/payments/" + orderId, PaymentResponse.class);
    }

    // 결제창 실패시 롤백 요청
    public Void rollbackPayment(PaymentFailRequest paymentFailRequest) {
        return backendApiClient.post(ORDER_SERVICE + "/payments/rollback", paymentFailRequest, Void.class);
    }

//    // 결제 취소인데 배송 전 전체 취소만 해당 - 출고일 이후는 포인트로 돌려줌
//    public PaymentResponse cancelPayment(PaymentCancelRequest cancelRequest, long orderId) {
//        return backendApiClient.post(ORDER_SERVICE + "/orders/" + orderId + "/cancel", cancelRequest, PaymentResponse.class);
//    }

}