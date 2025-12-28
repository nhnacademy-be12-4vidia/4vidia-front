package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.category.request.CreateCategoryRequest;
import com.nhnacademy._vidiafront.admin.dto.category.request.UpdateCategoryRequest;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCategoryManagementApiClient {

    private static final String CATEGORY_URL = "/api/v1/book-service/admin/categories";
    private final BackendApiClient backendApiClient;

    public void createCategory(CreateCategoryRequest request) {
        backendApiClient.post(CATEGORY_URL, request, Void.class);
    }

    public void updateCategory(Long categoryId, UpdateCategoryRequest request) {
        backendApiClient.put(CATEGORY_URL + "/" + categoryId, request, Void.class);
    }

    public void deleteCategory(Long categoryId) {
        backendApiClient.delete(CATEGORY_URL + "/" + categoryId, Void.class);
    }
}
