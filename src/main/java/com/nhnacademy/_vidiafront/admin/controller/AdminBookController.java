package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminBookApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookCreateRequest;
import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookController {

    private final AdminBookApiClient adminBookApiClient;
    private final BookApiClient bookApiClient;

    // 도서 생성 폼
    @GetMapping
    public String getBookCreateForm(
            Model model
    ) {
        model.addAttribute("mode", "create");
        return "admin/admin-book-form";
    }

    // 도서 생성 요청
    @PostMapping
    public String createBook(
            AdminBookCreateRequest request
    ) {
        adminBookApiClient.createBook(request);
        return "redirect:/admin/books";
    }

    // 도서 수정 폼
    @GetMapping("/{bookId}")
    public String getBookUpdateForm(
            @PathVariable Long bookId,
            Model model
    ) {
        BookDetailResponse bookDetails = bookApiClient.bookDetails(bookId).book();
        model.addAttribute("book", bookDetails);
        model.addAttribute("mode", "update");
        return "admin/admin-book-form";
    }

    // 도서 수정 요청
    @PutMapping("/{bookId}")
    public String updateBook(
            @PathVariable Long bookId,
            AdminBookCreateRequest request
    ) {
        adminBookApiClient.updateBook(bookId, request);
        return "redirect:/admin/books";
    }
}

