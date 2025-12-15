package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminBookApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookCreateRequest;
import com.nhnacademy._vidiafront.book.dto.books.response.BookDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookController {

    private final AdminBookApiClient bookApiClient;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("mode", "create");
        model.addAttribute("currentUri", "/admin/books/register");
        return "admin/admin-book-form";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AdminBookCreateRequest request) {
        bookApiClient.createBook(request);
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{bookId}")
    public String editForm(@PathVariable("bookId") Long bookId, Model model) {

        return "admin/admin-book-form";
    }

    @PostMapping("/update/{bookId}")
    public String update(@PathVariable("bookId") Long bookId, @ModelAttribute AdminBookCreateRequest request) {
        bookApiClient.updateBook(bookId, request);
        return "redirect:/admin/books";
    }
}

