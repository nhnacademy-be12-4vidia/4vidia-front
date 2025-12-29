package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchWithTagRequest;
import com.nhnacademy._vidiafront.book.dto.books.response.*;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;

import java.util.List;

import com.nhnacademy._vidiafront.global.dto.PageResponse;
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


        String uri = buildSearchUrl(BOOK_SERVICE + "/books/search", request, page, size);

        return backendApiClient.get(
                uri,
                new ParameterizedTypeReference<>() {}        );
    }

    public AiBookSearchResponse searchBooksWithLlm(BookSearchRequest request, int page, int size) {

        String uri = buildSearchUrl(BOOK_SERVICE + "/books/search/ai", request, page, size);

        return backendApiClient.get(
                uri,
                new ParameterizedTypeReference<>() {}        );
    }

    public PageResponse<BookListResponse> searchBooksWithTags(BookSearchWithTagRequest request, int page, int size) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath(BOOK_SERVICE + "/books/search/tags")
                .queryParam("page", page)
                .queryParam("size", size);

        if (request != null && request.tagNameList() != null) {
            for (String tag : request.tagNameList()) {
                if (tag == null || tag.isBlank()) continue;
                builder.queryParam("tagNameList", tag.trim());
            }
        }

        if (request != null && request.mode() != null) {
            builder.queryParam("mode", request.mode());
        }

        String uri = builder.toUriString();

        return backendApiClient.get(uri, new ParameterizedTypeReference<>() {});
    }

    public PageResponse<BookListResponse> searchBooksWithSpecificTagId(Long tagId, String tagName, int page, int size, String sortKey, String direction) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath(BOOK_SERVICE + "/books/search/tags/" + tagId)
                .queryParam("page", page)
                .queryParam("size", size);

        if (tagName != null && !tagName.isBlank()) {
            builder.queryParam("tagName", tagName);
        }
        if (sortKey != null && !sortKey.isBlank()) {
            builder.queryParam("sortKey", sortKey);
        }
        if (direction != null && !direction.isBlank()) {
            builder.queryParam("direction", direction);
        }

        String uri = builder.toUriString();

        return backendApiClient.get(uri, new ParameterizedTypeReference<>() {});
    }

    public BookDetailResponse bookDetails(Long bookId) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromPath(BOOK_SERVICE + "/books/" + bookId);

        return backendApiClient.get(uriBuilder.toUriString(), new ParameterizedTypeReference<>() {});

    }

    public List<BookListResponse> getBestSellerBook() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromPath(BOOK_SERVICE + "/books/best-seller");

        String uri = uriBuilder.toUriString();

        return backendApiClient.get(uri, new ParameterizedTypeReference<>() {});
    }

    public List<BookListResponse> searchBooksSimple(String keyword) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/books/search-simple").queryParam("keyword", keyword);

        String uri = uriBuilder.toUriString();

        return backendApiClient.get(uri, new ParameterizedTypeReference<>() {});
    }

    public List<BookListResponse> getMainBookList(Long tagId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/books/main-list").queryParam("tagId", tagId);

        String uri = uriBuilder.toUriString();

        return backendApiClient.get(uri, new ParameterizedTypeReference<>() {});
    }

    private String buildSearchUrl(String basePath, BookSearchRequest request, int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(basePath)
                .queryParam("keyword", request.keyword() == null ? "" : request.keyword())
                .queryParam("page", page)
                .queryParam("size", size);


        if (request.sort() != null) {
            builder.queryParam("sortKey", getSortKey(request));
            builder.queryParam("direction", getDirection(request));
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

    private String getSortKey(BookSearchRequest request) {
        return switch (request.sort()) {
            case PUBLISHED_DESC, PUBLISHED_ASC -> "publishedDate";
            case PRICE_DESC, PRICE_ASC -> "priceSales";
            case RATING_DESC -> "avgRating";
            default -> "publishedDate";
        };
    }

    private String getDirection(BookSearchRequest request) {
        return switch (request.sort()) {
            case PUBLISHED_ASC, PRICE_ASC -> "asc";
            default -> "desc";
        };
    }
}
