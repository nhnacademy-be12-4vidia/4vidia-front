package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.CouponPolicyUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.CouponPolicyResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<CouponPolicyResponse> getPolicyList() {
        return backendApiClient.get(
                COUPON_SERVICE + "/policies/all",
                new ParameterizedTypeReference<>() {}
        );
    }

    // 활성/비활성 toggle
    public void toggleActivation(Long policyId) {
        backendApiClient.patchNoBody(
                COUPON_SERVICE + "/policies/" + policyId + "/toggle",
                Void.class
        );
    }

    // 정책 단건 조회
    public CouponPolicyResponse getPolicy(Long policyId) {
        return backendApiClient.get(
                COUPON_SERVICE + "/policies/" + policyId,
                CouponPolicyResponse.class
        );
    }

    // 정책 수정
    public void updatePolicy(Long policyId, CouponPolicyUpdateRequest request) {
        backendApiClient.put(
                COUPON_SERVICE + "/policies/" + policyId,
                request,
                Void.class
        );
    }

}
