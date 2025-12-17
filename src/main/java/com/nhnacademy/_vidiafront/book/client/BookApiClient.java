package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.books.request.BookBestRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.response.AiBookSearchResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailWithReviewResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookListResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.SearchBooksResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookApiClient {

    private final BackendApiClient backendApiClient;

    private static final String BOOK_SERVICE = "/api/v1/book-service";

    /**
     * 도서 검색
     */

    public SearchBooksResponse searchBooks(BookSearchRequest request, int page, int size) {

        String url = buildSearchUrl(BOOK_SERVICE + "/books/search", request, page, size);

        return backendApiClient.get(
            url,
            SearchBooksResponse.class
        );
    }

    public AiBookSearchResponse searchBooksWithLlm(BookSearchRequest request, int page, int size) {

        String url = buildSearchUrl(BOOK_SERVICE + "/books/search/ai", request, page, size);

        return backendApiClient.get(
            url,
            AiBookSearchResponse.class
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

    private String buildSearchUrl(String basePath, BookSearchRequest request, int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(basePath)
                .queryParam("keyword", request.keyword() == null ? "" : request.keyword())
                .queryParam("page", page)
                .queryParam("size", size);


        if (request.sort() != null) {
            builder.queryParam("sort", request.sort());
        }
        if (request.categoryId() != null) {
            builder.queryParam("categoryId", request.categoryId());
        }
        if (request.minPrice() != null) {
            builder.queryParam("minPrice", request.minPrice());
        }
        if (request.maxPrice() != null) {
            builder.queryParam("maxPrice", request.maxPrice());
        }
        if (Boolean.TRUE.equals(request.useSemantic())) {
            builder.queryParam("useSemantic", true);
        }

        return builder.toUriString();
    }
}
