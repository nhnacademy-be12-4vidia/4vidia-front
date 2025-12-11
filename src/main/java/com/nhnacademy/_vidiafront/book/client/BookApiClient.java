package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.books.request.BookBestRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.response.AiBookSearchResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailWithReviewResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookListResponse;
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

    public PageResponse<BookListResponse> searchBooks(BookSearchRequest request, int page, int size) {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(BOOK_SERVICE).append("/books/search");

        urlBuilder.append("?keyword=");
        if (request.keyword() != null) {
            urlBuilder.append(request.keyword());
        }

        urlBuilder.append("&page=").append(page);
        urlBuilder.append("&size=").append(size);

        if (request.sort() != null) {
            urlBuilder.append("&sort=").append(request.sort());
        }
        if (request.categoryId() != null) {
            urlBuilder.append("&categoryId=").append(request.categoryId());
        }
        if (request.minPrice() != null) {
            urlBuilder.append("&minPrice=").append(request.minPrice());
        }
        if (request.maxPrice() != null) {
            urlBuilder.append("&maxPrice=").append(request.maxPrice());
        }
        if (Boolean.TRUE.equals(request.useSemantic())) {
            urlBuilder.append("&useSemantic=true");
        }

        String url = urlBuilder.toString();

        return backendApiClient.get(
            url,
            new ParameterizedTypeReference<PageResponse<BookListResponse>>() {}
        );
    }

    public AiBookSearchResponse searchBooksWithLlm(BookSearchRequest request, int page, int size) {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(BOOK_SERVICE).append("/books/search/ai");

        urlBuilder.append("?keyword=");
        if (request.keyword() != null) {
            urlBuilder.append(request.keyword());
        }

        urlBuilder.append("&page=").append(page);
        urlBuilder.append("&size=").append(size);

        if (request.sort() != null) {
            urlBuilder.append("&sort=").append(request.sort());
        }
        if (request.categoryId() != null) {
            urlBuilder.append("&categoryId=").append(request.categoryId());
        }
        if (request.minPrice() != null) {
            urlBuilder.append("&minPrice=").append(request.minPrice());
        }
        if (request.maxPrice() != null) {
            urlBuilder.append("&maxPrice=").append(request.maxPrice());
        }
        if (Boolean.TRUE.equals(request.useSemantic())) {
            urlBuilder.append("&useSemantic=true");
        }

        String url = urlBuilder.toString();

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
}
