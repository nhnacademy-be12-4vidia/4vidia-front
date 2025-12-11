package com.nhnacademy._vidiafront.refund.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.refund.dto.request.RefundRequest;
import com.nhnacademy._vidiafront.refund.dto.response.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    /**
     * 반품 가능 도서 조회
     */
    public RefundResponse getRefundList(long orderId){
        return backendApiClient.get(ORDER_SERVICE + "/orders/" + orderId + "/refunds" , RefundResponse.class);
    }

    /**
     * 반품 신청
     */
    public void refundRegister(RefundRequest refundRequest){
        backendApiClient.post(ORDER_SERVICE + "/refunds", refundRequest, Void.class);
    }

}
