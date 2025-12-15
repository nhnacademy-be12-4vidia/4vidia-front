package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiafront.coupon.dto.CouponPolicyDto;
import com.nhnacademy._vidiafront.coupon.dto.PageDto;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AdminCouponApiClient {

    private static final String COUPON_SERVICE = "/api/v1/coupon";

    private final BackendApiClient backendApiClient;

    public void createPolicy(CouponPolicyCreateRequest request) {
        backendApiClient.post(
                COUPON_SERVICE + "/policies",
                request,
                Void.class
        );
    }

    // 활성/비활성 toggle
    public void toggleActivation(Long policyId) {
        backendApiClient.patchNoBody(
                COUPON_SERVICE + "/policies/" + policyId + "/toggle",
                Void.class
        );
    }

    // 쿠폰 정책 검색 + 페이징
    public PageDto<CouponPolicyDto> searchPolicies(
            String keyword,
            String status,
            String targetType,
            int page,
            int size
    ) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromPath(COUPON_SERVICE + "/policies/search")
                .queryParam("page", page)
                .queryParam("size", size);

        if (keyword != null && !keyword.isBlank()) {
            uriBuilder.queryParam("keyword", keyword);
        }
        if (status != null && !"ALL".equals(status)) {
            uriBuilder.queryParam("status", status);
        }
        if (targetType != null && !"ALL".equals(targetType)) {
            uriBuilder.queryParam("targetType", targetType);
        }

        return backendApiClient.get(
                uriBuilder.build().toUriString(),
                new ParameterizedTypeReference<PageDto<CouponPolicyDto>>() {}
        );
    }


}
