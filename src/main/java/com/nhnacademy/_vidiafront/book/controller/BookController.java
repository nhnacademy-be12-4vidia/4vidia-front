package com.nhnacademy._vidiafront.book.controller;

import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.dto.books.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.books.response.AiBookSearchResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailWithReviewResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookListResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookApiClient bookApiClient;

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

            model.addAttribute("page", pageResponse);              // 페이징 정보 그대로
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

        PageResponse<BookListResponse> pageResult = bookApiClient.searchBooks(request, page, size);

        model.addAttribute("books", pageResult.content());
        model.addAttribute("page", pageResult.page());
        model.addAttribute("size", pageResult.size());
        model.addAttribute("totalPages", pageResult.totalPages());
        model.addAttribute("totalElements", pageResult.totalElements());
        model.addAttribute("keyword", request.keyword());
        model.addAttribute("useSemantic", request.useSemantic());

        return "book/search";

    }
//
//    @GetMapping("/search/ai")
//    public String searchBooksWithLlm(BookSearchRequest request,
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "20") int size,
//        Model model) {
//
//        if (!StringUtils.hasText(request.keyword())) {
//            model.addAttribute("books", List.of());
//            model.addAttribute("keyword", "");
//            model.addAttribute("message", "검색어를 입력해주세요.");
//            return "book/search";
//        }
//
//        AiBookSearchResponse response = bookApiClient.searchBooksWithLlm(request, page, size);
//
//
//        PageResponse<BookListResponse> pageResult = response.results();
//
//        model.addAttribute("books", pageResult.content());
//        model.addAttribute("page", pageResult.page());
//        model.addAttribute("size", pageResult.size());
//        model.addAttribute("totalPages", pageResult.totalPages());
//        model.addAttribute("totalElements", pageResult.totalElements());
//        model.addAttribute("keyword", request.keyword());
//        model.addAttribute("useSemantic", request.useSemantic());
//
//        // AI 관련 추가 필드
//        model.addAttribute("useAi", true);
//        model.addAttribute("aiAnswer", response.aiAnswer());
//
//        return "book/search";
//
//
//    }

    @GetMapping("/{bookId}")
    public String bookDetails(@PathVariable Long bookId, Model model) {

        BookDetailWithReviewResponse dto = bookApiClient.bookDetails(bookId);
        model.addAttribute("book", dto.book());
        model.addAttribute("reviewPage", dto.reviews());
        model.addAttribute("reviews", dto.reviews().content());

        return "book/detailView";

    }
}
