package com.nhnacademy._vidiafront.refund.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.refund.dto.request.RefundRequest;
import com.nhnacademy._vidiafront.refund.dto.response.RefundResponse;
import com.nhnacademy._vidiafront.refund.dto.response.RefundHistoryGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RefundApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    /**
     * 반품 가능 도서 조회
     */
    public RefundResponse getRefundList(long orderId){
        return backendApiClient.get(ORDER_SERVICE + "/orders/" + orderId + "/refunds" , new ParameterizedTypeReference<>() {});
    }

    /**
     * 반품 신청
     */
    public void refundRegister(RefundRequest refundRequest){
        backendApiClient.post(ORDER_SERVICE + "/refunds", refundRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 사용자 반품 내역 조회
     */
    public List<RefundHistoryGroupResponse> refundHistory(String status) {
        if (status == null) {
            return backendApiClient.get(
                    ORDER_SERVICE + "/users/me/refunds",
                    new ParameterizedTypeReference<>() {}
            );
        }

        return backendApiClient.get(
                ORDER_SERVICE + "/users/me/refunds?status=" + status,
                new ParameterizedTypeReference<>() {}
        );
    }


}
