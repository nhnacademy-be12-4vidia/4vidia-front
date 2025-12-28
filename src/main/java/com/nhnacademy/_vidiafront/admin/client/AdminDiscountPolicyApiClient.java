package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.DiscountPolicyCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.DiscountPolicyUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.DiscountPolicyResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDiscountPolicyApiClient {

    private static final String DISCOUNT_POLICY_URL = "/api/v1/book-service/admin/books/discount-policies";
    private final BackendApiClient backendApiClient;

    public List<DiscountPolicyResponse> getPolicies(Long categoryId) {
        String url = DISCOUNT_POLICY_URL;
        if (categoryId != null) {
            url += "?categoryId=" + categoryId;
        }
        return backendApiClient.get(url, new ParameterizedTypeReference<ApiResponse<List<DiscountPolicyResponse>>>() {});
    }

    public DiscountPolicyResponse getPolicy(Long id) {
        String url = DISCOUNT_POLICY_URL + "/" + id;
        return backendApiClient.get(url, new ParameterizedTypeReference<ApiResponse<DiscountPolicyResponse>>() {});
    }

    public void createPolicy(DiscountPolicyCreateRequest request) {
        backendApiClient.post(DISCOUNT_POLICY_URL, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public void updatePolicy(Long id, DiscountPolicyUpdateRequest request) {
        String url = DISCOUNT_POLICY_URL + "/" + id;
        backendApiClient.put(url, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public void deletePolicy(Long id) {
        String url = DISCOUNT_POLICY_URL + "/" + id;
        backendApiClient.delete(url, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }
}
