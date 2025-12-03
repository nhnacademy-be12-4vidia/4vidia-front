package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.pointpolicy.request.PointPolicyRequest;
import com.nhnacademy._vidiafront.admin.dto.pointpolicy.response.PointPolicyResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointPolicyApiClient {
    private static final String POLICY_SERVICE = "/api/v1/policy-service";

    private final BackendApiClient backendApiClient;

    public List<PointPolicyResponse> getPointPolicyList(){
        return backendApiClient.get(POLICY_SERVICE + "/point-policies", new ParameterizedTypeReference<>(){});
    }

    public PointPolicyResponse updatePointPolicy(Long pointPolicyId, PointPolicyRequest pointPolicyRequest){
        // patch는..?
        return backendApiClient.put(POLICY_SERVICE + "/point-policies/" + pointPolicyId, pointPolicyRequest , PointPolicyResponse.class);
    }
}

