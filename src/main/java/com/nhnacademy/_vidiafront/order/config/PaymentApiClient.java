package com.nhnacademy._vidiafront.order.config;

import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentCancelRequest;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.exception.ApiPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {
    private final RestClient restClient;
    private static final String ORDER_SERVICE = "/api/v1/order-service";

    private static final String TEST_ID = "1";

    // 결제 확정 및 결제 저장
    public void confirmPayment(PaymentConfirmRequest confirmRequest, long id) {
        try {
            restClient.post()
                    .uri(ORDER_SERVICE + "/orders/{orderId}/success", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .body(confirmRequest)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new ApiPaymentException("api 결제 확정 및 저장 실패" + e.getMessage());
        }
    }

    //TODO 결제 취소(부분반품)인데 배송 전 전체 취소만 해당 - 출고일 이후는 포인트로 돌려줌
    public void cancelPayment(PaymentCancelRequest cancelRequest, long orderId) {
        try {
            restClient.post()
                    .uri(ORDER_SERVICE + "/orders/{orderId}/cancel", orderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-User-Id", TEST_ID) //
                    .body(cancelRequest)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new ApiPaymentException("api 결제 취소 실패" + e.getMessage());
        }
    }






}