package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.response.AdminRefundListResponse;
import com.nhnacademy._vidiafront.admin.dto.response.RefundDetailResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AdminRefundApiClient {
    private static final String ORDER_SERVICE = "/api/v1/order-service";
    private final BackendApiClient backendApiClient;

    /**
     * 관리자 반품 목록 조회 (페이징 + 상태)
     */
    public PageResponse<AdminRefundListResponse> getRefundList(String refundStatus, String keyword, int page, int size){
        var builder = UriComponentsBuilder
                .fromPath(ORDER_SERVICE+"/admin/refunds")
                .queryParam("page",page)
                .queryParam("size",size);

        if (keyword != null){
            builder.queryParam("keyword",keyword);
        }
        if(refundStatus != null){
            builder.queryParam("refundStatus",refundStatus);
        }

        String url = builder.toUriString();

        return backendApiClient.get(url, new ParameterizedTypeReference<PageResponse<AdminRefundListResponse>>() {
        });

    }

    public RefundDetailResponse getRefundDetail(Long id) {
        String url = ORDER_SERVICE + "/admin/refunds/" + id;
        return backendApiClient.get(url, new ParameterizedTypeReference<RefundDetailResponse>() {});
    }

    public void acceptRefund(Long id) {
        String url = ORDER_SERVICE + "/admin/refunds/" + id + "/accept";
        backendApiClient.postNoBody(url, Void.class); // body 없으면 null
    }

    public void rejectRefund(Long id) {
        String url = ORDER_SERVICE + "/admin/refunds/" + id + "/reject";
        backendApiClient.postNoBody(url, Void.class);
    }


}
