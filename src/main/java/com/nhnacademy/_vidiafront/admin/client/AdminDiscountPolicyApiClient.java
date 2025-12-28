package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.DiscountPolicyCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.DiscountPolicyUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.DiscountPolicyResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
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
        return backendApiClient.get(url, new ParameterizedTypeReference<List<DiscountPolicyResponse>>() {});
    }

    public DiscountPolicyResponse getPolicy(Long id) {
        String url = DISCOUNT_POLICY_URL + "/" + id;
        return backendApiClient.get(url, DiscountPolicyResponse.class);
    }

    public void createPolicy(DiscountPolicyCreateRequest request) {
        backendApiClient.post(DISCOUNT_POLICY_URL, request, Void.class);
    }

    public void updatePolicy(Long id, DiscountPolicyUpdateRequest request) {
        String url = DISCOUNT_POLICY_URL + "/" + id;
        backendApiClient.put(url, request, Void.class);
    }

    public void deletePolicy(Long id) {
        String url = DISCOUNT_POLICY_URL + "/" + id;
        backendApiClient.delete(url, Void.class);
    }
}
