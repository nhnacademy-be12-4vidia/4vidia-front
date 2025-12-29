package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.category.request.CreateCategoryRequest;
import com.nhnacademy._vidiafront.admin.dto.category.request.UpdateCategoryRequest;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCategoryManagementApiClient {

    private static final String CATEGORY_URL = "/api/v1/book-service/admin/categories";
    private final BackendApiClient backendApiClient;

    public void createCategory(CreateCategoryRequest request) {
        backendApiClient.post(CATEGORY_URL, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public void updateCategory(Long categoryId, UpdateCategoryRequest request) {
        backendApiClient.put(CATEGORY_URL + "/" + categoryId, request, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public void deleteCategory(Long categoryId) {
        backendApiClient.delete(CATEGORY_URL + "/" + categoryId, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }
}
