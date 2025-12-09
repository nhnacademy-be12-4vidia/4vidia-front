package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.books.request.BookBestRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailWithReviewResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookListResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class BookApiClient {

    private final BackendApiClient backendApiClient;

    private static final String BOOK_SERVICE = "/api/v1/book-service";

    /**
     * 도서 검색
     */

    public PageResponse<BookListResponse> searchBooks(BookSearchRequest request, int page, int size) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromPath(BOOK_SERVICE + "/books/search")
            .queryParam("keyword", request.keyword())
            .queryParam("page", page)
            .queryParam("size", size);

        if (request.sort() != null) {
            uriBuilder.queryParam("sort", request.sort());
        }
        if (request.categoryId() != null) {
            uriBuilder.queryParam("categoryId", request.categoryId());
        }
        if (request.minPrice() != null) {
            uriBuilder.queryParam("minPrice", request.minPrice());
        }
        if (request.maxPrice() != null) {
            uriBuilder.queryParam("maxPrice", request.maxPrice());
        }
        if (Boolean.TRUE.equals(request.useSemantic())) {
            uriBuilder.queryParam("useSemantic", true);
        }

        String url = uriBuilder.toUriString();

        return backendApiClient.get(url, new ParameterizedTypeReference<PageResponse<BookListResponse>>() {}
        );
    }

    public BookDetailWithReviewResponse bookDetails(Long bookId) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromPath(BOOK_SERVICE + "/books/" + bookId);

        return backendApiClient.get(uriBuilder.toUriString(), BookDetailWithReviewResponse.class);

    }

    public List<BookListResponse> getBestSellerBook() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromPath(BOOK_SERVICE + "/books/best-seller");

        String uri = uriBuilder.toUriString();

        return backendApiClient.get(uri, new ParameterizedTypeReference<List<BookListResponse>>() {});
    }
}
