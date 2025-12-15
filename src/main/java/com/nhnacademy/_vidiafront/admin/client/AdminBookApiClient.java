package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminBookCreateRequest;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBookApiClient {
    private static final String BOOK_SERVICE = "/api/v1/book-service";

    private final BackendApiClient backendApiClient;

    public void createBook(AdminBookCreateRequest request) {
        String url = BOOK_SERVICE + "/admin/books";
        backendApiClient.post(url, request, Void.class);
    }

    public void updateBook(Long bookId, AdminBookCreateRequest request) {
        String url = BOOK_SERVICE + "/admin/books/" + bookId;
        backendApiClient.put(url, request, Void.class);
    }
}

