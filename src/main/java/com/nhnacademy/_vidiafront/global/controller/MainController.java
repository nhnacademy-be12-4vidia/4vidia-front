package com.nhnacademy._vidiafront.global.controller;

import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.dto.books.response.BookListResponse;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final BookApiClient bookApiClient;
    private final UserApiClient userApiClient;

    @GetMapping("/")
    public String index(Model model) {
        List<BookListResponse> bestSellerBook = bookApiClient.getBestSellerBook();
        model.addAttribute("bestSellerBook", bestSellerBook);
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        if (userApiClient.getUserRole().equals("ADMIN")) {
            return "admin/admin-layout";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "redirect:/mypage/profile";
    }
}
