package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.book.dto.books.response.CategoryResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminCategoryApiClient {

    private static final String COUPON_SERVICE = "/api/v1/coupon-service";

    private final BackendApiClient backendApiClient;

    public List<CategoryResponse> getCategoryList() {
        return backendApiClient.get(
                COUPON_SERVICE + "/categories",
                new ParameterizedTypeReference<>() {}
        );
    }
}
