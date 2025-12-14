package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminBookSearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminBookResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AdminBookApiClient {
    private static final String BOOK_SERVICE = "/api/v1/book-service";

    private final BackendApiClient backendApiClient;

    public PageResponse<AdminBookResponse> getBooks(AdminBookSearchRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/admin/books")
                .queryParam("page", request.pageOrDefault())
                .queryParam("size", request.sizeOrDefault());

        String isbn = request.isbnOrNull();
        if (isbn != null) {
            builder.queryParam("isbn", isbn);
        }

        String title = request.titleOrNull();
        if (title != null) {
            builder.queryParam("title", title);
        }

        String url = builder.build().toUriString();
        return backendApiClient.get(url, new ParameterizedTypeReference<>() {});
    }

    public BookDetailResponse getBook(Long bookId) {
        String url = BOOK_SERVICE + "/books/" + bookId;
        return backendApiClient.get(url, BookDetailResponse.class);
    }
}

