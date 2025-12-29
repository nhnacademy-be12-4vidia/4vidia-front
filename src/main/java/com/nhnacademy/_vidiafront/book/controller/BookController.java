package com.nhnacademy._vidiafront.book.controller;

import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.client.ReviewApiClient;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchWithTagRequest;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSortOptions;
import com.nhnacademy._vidiafront.book.dto.books.response.*;
import com.nhnacademy._vidiafront.book.dto.reviews.request.ReviewUpdateRequest;
import com.nhnacademy._vidiafront.book.dto.reviews.response.ReviewListWithSummaryResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;

import java.io.IOException;
import java.util.List;

import com.nhnacademy._vidiafront.user.client.LikeApiClient;
import groovy.util.logging.Slf4j;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.HttpStatus.*;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
@Slf4j
public class BookController {

    private final BookApiClient bookApiClient;
    private final ReviewApiClient reviewApiClient;
    private final LikeApiClient likeApiClient;

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

    @DeleteMapping("/like")
    @ResponseBody
    public ResponseEntity<Void> deleteLikeTest(@RequestParam Long bookId) {
        try {
            likeApiClient.deleteLike(bookId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<Void> addLikeTest(@RequestParam Long bookId) {
        try {
            likeApiClient.addLike(bookId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/{book-id}/reviews/{review-id}/deactivate")
    public String deactivateReview(@PathVariable(name = "book-id") Long bookId,
                                   @PathVariable(name = "review-id") Long reviewId,
                                   RedirectAttributes redirectAttributes) {

        try {
            reviewApiClient.deactivateReview(reviewId, bookId);
            redirectAttributes.addFlashAttribute("toast", "리뷰가 삭제되었습니다.");
            return "redirect:/books/" + bookId;
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode()) {
                case FORBIDDEN -> redirectAttributes.addFlashAttribute("toast", "본인이 작성한 리뷰만 삭제할 수 있습니다.");
                case NOT_FOUND -> redirectAttributes.addFlashAttribute("toast", "리뷰를 찾을 수 없습니다.");
                case CONFLICT -> redirectAttributes.addFlashAttribute("toast", "이미 삭제된 리뷰입니다.");
                default -> redirectAttributes.addFlashAttribute("toast", "리뷰 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "리뷰 삭제중 오류가 발생했습니다.");
        }
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/{book-id}/reviews/{review-id}/edit")
    public String reviewEditForm(@PathVariable(name = "book-id") Long bookId,
                                 @PathVariable(name = "review-id") Long reviewId,
                                 @RequestHeader(value = "Referer", required = false) String referer,
                                 Model model) {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder().reviewId(reviewId).bookId(bookId).build();

        String returnUrl = (referer != null && !referer.isBlank()) ? referer : ("/books/" + bookId);

        model.addAttribute("request", request);
        model.addAttribute("returnUrl", returnUrl);

        return "review/reviewUpdateForm";

    }

    @PostMapping("/{book-id}/reviews/{review-id}/edit")
    public String editReview(@PathVariable(name = "book-id") Long bookId, @PathVariable(name = "review-id") Long reviewId,
                                           @ModelAttribute ReviewUpdateRequest request,
                                           @RequestParam(name = "images", required = false)List<MultipartFile> images,
                             @RequestParam(name = "returnUrl", required = false) String returnUrl,
                             RedirectAttributes redirectAttributes) throws IOException {

        reviewApiClient.editReview(request, images);

        String redirectUrl = (returnUrl != null && !returnUrl.isBlank()) ? returnUrl : ("/books/" + bookId);
        redirectAttributes.addFlashAttribute("toast", "리뷰가 수정되었습니다.");


        return "redirect:" + redirectUrl;
    }
}
