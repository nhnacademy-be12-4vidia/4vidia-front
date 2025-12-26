package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.GradePolicyUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.PointPolicyRequest;
import com.nhnacademy._vidiafront.admin.dto.response.GradePolicyResponse;
import com.nhnacademy._vidiafront.admin.dto.response.PointPolicyResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GradePolicyApiClient {
    private static final String POLICY_SERVICE = "/api/v1/user-service";

    private final BackendApiClient backendApiClient;

    public GradePolicyResponse getGradePolicy(Long gradePolicyId) {
        return backendApiClient.get(POLICY_SERVICE + "/admin/grade-policies/" + gradePolicyId, GradePolicyResponse.class);
    }

    public List<GradePolicyResponse> getGradePolicyList(){
        return backendApiClient.get(POLICY_SERVICE + "/admin/grade-policies", new ParameterizedTypeReference<>(){});
    }

    public void updatePointPolicy(Long gradeId, GradePolicyUpdateRequest gradePolicyUpdateRequest){
        backendApiClient.put(POLICY_SERVICE + "/admin/grade-policies/" + gradeId, gradePolicyUpdateRequest , Void.class);
    }
}

