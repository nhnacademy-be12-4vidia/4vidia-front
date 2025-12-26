package com.nhnacademy._vidiafront.book.controller;

import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.client.ReviewApiClient;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchWithTagRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSortOptions;
import com.nhnacademy._vidiafront.book.dto.books.response.*;
import com.nhnacademy._vidiafront.book.dto.reviews.response.ReviewListWithSummaryResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import java.util.List;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
@Slf4j
public class BookController {

    private final BookApiClient bookApiClient;
    private final ReviewApiClient reviewApiClient;

    /**
     * 도서 검색
     */

    @GetMapping("/search")
    public String searchBooks(BookSearchRequest request,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {

        boolean useSemantic = Boolean.TRUE.equals(request.useSemantic());

        if (useSemantic) {
            AiBookSearchResponse aiResponse = bookApiClient.searchBooksWithLlm(request, page, size);

            PageResponse<BookListResponse> pageResponse = aiResponse.results();

            model.addAttribute("keyword", request.keyword());
            model.addAttribute("useSemantic", true);

            model.addAttribute("page", pageResponse.page());
            model.addAttribute("size", pageResponse.size());
            model.addAttribute("totalPages", pageResponse.totalPages());
            model.addAttribute("totalElements", pageResponse.totalElements());
            model.addAttribute("books", pageResponse.content()); // PageResponse 안의 리스트
            model.addAttribute("aiAnswer", aiResponse.aiAnswer());

            return "book/search-ai";  //
        }

        if (!StringUtils.hasText(request.keyword())) {
            model.addAttribute("books", List.of());
            model.addAttribute("keyword", "");
            model.addAttribute("message", "검색어를 입력해주세요.");
            return "book/search";
        }

        SearchBooksResponse response = bookApiClient.searchBooks(request, page, size);
        PageResponse<BookListResponse> pageResult = response.page();
        List<AiCacheResponse> aiCacheResponseList = response.aiCacheResponseList();

        model.addAttribute("books", pageResult.content());
        model.addAttribute("page", pageResult.page());
        model.addAttribute("size", pageResult.size());
        model.addAttribute("totalPages", pageResult.totalPages());
        model.addAttribute("totalElements", pageResult.totalElements());
        model.addAttribute("keyword", request.keyword());
        model.addAttribute("useSemantic", request.useSemantic());
        model.addAttribute("aiCacheResponseList", aiCacheResponseList);

        return "book/search";
    }

    @GetMapping("/search/tags")
    public String searchBooksWithTags(BookSearchWithTagRequest request,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      Model model) {
        PageResponse<BookListResponse> pageResult = bookApiClient.searchBooksWithTags(request, page, size);

        model.addAttribute("books", pageResult.content());
        model.addAttribute("page", pageResult.page());
        model.addAttribute("size", pageResult.size());
        model.addAttribute("totalPages", pageResult.totalPages());
        model.addAttribute("totalElements", pageResult.totalElements());
        model.addAttribute("tagRequest", request);

        return "book/search-with-tag";
    }

    @GetMapping("/{bookId:\\d+}")
    public String bookDetails(@PathVariable Long bookId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {

        BookDetailResponse bookDetailResponse = bookApiClient.bookDetails(bookId);
        ReviewListWithSummaryResponse reviewListWithSummary = reviewApiClient.getReviewsWithSummary(bookId, page, size);

        model.addAttribute("book", bookDetailResponse);
        model.addAttribute("reviewPage", reviewListWithSummary.reviews());
        model.addAttribute("reviews", reviewListWithSummary.reviews().content());
        model.addAttribute("reviewSummary", reviewListWithSummary.reviewSummary());

        return "book/detailView";
    }

    @GetMapping("/search/tags/{tag-id}")
    public String searchBooksWithSpecificTagId(@PathVariable(name = "tag-id") Long tagId,
                                               @RequestParam(required = false, name = "tagName") String tagName,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size,
                                               @RequestParam(defaultValue = "PUBLISHED_DESC") BookSortOptions sort,
                                               Model model) {
        String sortKey = switch (sort) {
            case PUBLISHED_DESC, PUBLISHED_ASC -> "publishedDate";
            case PRICE_DESC, PRICE_ASC -> "priceSales";
            case RATING_DESC -> "avgRating";
        };

        String direction = switch (sort) {
            case PUBLISHED_ASC, PRICE_ASC -> "asc";
            default -> "desc";
        };


        PageResponse<BookListResponse> pageResult = bookApiClient.searchBooksWithSpecificTagId(tagId, tagName, page, size, sortKey, direction);

        model.addAttribute("books", pageResult.content());
        model.addAttribute("page", pageResult.page());
        model.addAttribute("size", pageResult.size());
        model.addAttribute("totalPages", pageResult.totalPages());
        model.addAttribute("totalElements", pageResult.totalElements());
        model.addAttribute("tagName", tagName);

        return "book/search-by-tag";
    }

    @GetMapping("/api/main-list")
    @ResponseBody
    public List<BookListResponse> getMainBookList(@RequestParam(defaultValue = "0") Long tagId) {

        return bookApiClient.getMainBookList(tagId);
    }
}
