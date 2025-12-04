package com.nhnacademy._vidiafront.book.controller;

import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.dto.request.BookSearchRequest;
import com.nhnacademy._vidiafront.book.dto.response.BookListResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

}
