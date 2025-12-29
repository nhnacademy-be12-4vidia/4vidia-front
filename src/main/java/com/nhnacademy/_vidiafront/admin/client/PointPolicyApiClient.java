package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.PointPolicyRequest;
import com.nhnacademy._vidiafront.admin.dto.response.PointPolicyResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointPolicyApiClient {
    private static final String POLICY_SERVICE = "/api/v1/user-service";

    private final BackendApiClient backendApiClient;

    public PointPolicyResponse getPointPolicy(Long pointPolicyId) {
        return backendApiClient.get(POLICY_SERVICE + "/admin/point-policies/" + pointPolicyId,  new ParameterizedTypeReference<>() {});
    }

    public List<PointPolicyResponse> getPointPolicyList(){
        return backendApiClient.get(POLICY_SERVICE + "/admin/point-policies", new ParameterizedTypeReference<>(){});
    }

    public void updatePointPolicy(Long pointPolicyId, PointPolicyRequest pointPolicyRequest){
        backendApiClient.put(POLICY_SERVICE + "/admin/point-policies/" + pointPolicyId, pointPolicyRequest , new ParameterizedTypeReference<>() {});
    }
}

