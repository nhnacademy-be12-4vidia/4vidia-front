package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.categories.response.CategoryResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryApiClient {

    private final BackendApiClient backendApiClient;

    private static final String BOOK_SERVICE = "/api/v1/book-service";

    public List<CategoryResponse> getFlatCategoryList() {
        return backendApiClient.get(
                BOOK_SERVICE + "/categories/flat",
                new ParameterizedTypeReference<>() {}
        );
    }
}
