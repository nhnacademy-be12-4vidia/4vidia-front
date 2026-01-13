package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiafront.coupon.dto.CouponPolicyDto;
import com.nhnacademy._vidiafront.coupon.dto.response.MyCouponResponse;
import com.nhnacademy._vidiafront.coupon.dto.PageDto;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminCouponApiClient {

    private static final String COUPON_SERVICE = "/api/v1/coupon-service";

    private final BackendApiClient backendApiClient;

    public void createPolicy(CouponPolicyCreateRequest request) {
        backendApiClient.post(
                COUPON_SERVICE + "/admin/policies",
                request,
                new ParameterizedTypeReference<ApiResponse<Void>>(){}
        );
    }

    // 활성/비활성 toggle
    public void toggleActivation(Long policyId) {
        backendApiClient.patchNoBody(
                COUPON_SERVICE + "/admin/policies/" + policyId + "/toggle",
                new ParameterizedTypeReference<ApiResponse<Void>>(){}
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
                .fromPath(COUPON_SERVICE + "/admin/policies/search")
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
                uriBuilder.encode().build().toUriString(),
                new ParameterizedTypeReference<>() {}
        );
    }

    // 유저 쿠폰 목록 조회
    public List<MyCouponResponse> getUserCoupons(Long userId) {
        return backendApiClient.get(
                COUPON_SERVICE+"/admin/users/" + userId + "/coupons",
                new ParameterizedTypeReference<>() {}
        );
    }

    // 유저에게 재고제한 쿠폰 발급
    public void issueCouponToUser(Long userId, Long policyId) {
        backendApiClient.postNoBody(
                COUPON_SERVICE+"/admin/users/" + userId + "/coupons/" + policyId + "/issue",
                new ParameterizedTypeReference<ApiResponse<Void>>(){}        );
    }

    // 유저에게 무제한쿠폰발급
    public void issueEventCouponToUser(Long  userId, Long policyId) {
        backendApiClient.postNoBody(
                COUPON_SERVICE+"/admin/users/"+userId+"/coupons/"+policyId + "/issue-event",
                new ParameterizedTypeReference<ApiResponse<Void>>(){}        );
    }

    public List<CouponPolicyDto> getIssuablePolicies(Long userId) {
        return backendApiClient.get(
                COUPON_SERVICE + "/admin/users/" + userId + "/coupons/issuable-policies",
                new ParameterizedTypeReference<>() {}
        );
    }




}
