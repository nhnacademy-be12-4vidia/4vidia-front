package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminBookApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookSearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminBookResponse;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookController {

    private final AdminBookApiClient bookApiClient;

    @GetMapping
    public String list(@RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestParam(required = false) String isbn,
                       @RequestParam(required = false) String title,
                       Model model) {

        AdminBookSearchRequest request = new AdminBookSearchRequest(page, size, isbn, title);
        PageResponse<AdminBookResponse> booksPage = bookApiClient.getBooks(request);
        
        model.addAttribute("isbn", isbn);
        model.addAttribute("title", title);
        model.addAttribute("page", booksPage);
        model.addAttribute("currentUri", "/admin/books");

        return "admin/admin-book-list";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("mode", "create");
        model.addAttribute("currentUri", "/admin/books/register");
        return "admin/admin-book-form";
    }

    @GetMapping("/edit/{bookId}")
    public String editForm(@PathVariable("bookId") Long bookId, Model model) {
        BookDetailResponse book = bookApiClient.getBook(bookId);
        model.addAttribute("book", book);
        model.addAttribute("mode", "update");
        model.addAttribute("currentUri", "/admin/books");
        return "admin/admin-book-form";
    }
}

