package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.dto.books.response.BookListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookCouponController {

    private final BookApiClient bookApiClient;

    @GetMapping("/search-simple")
    public List<BookListResponse> searchBooksSimple(
            @RequestParam String keyword
    ) {
        return bookApiClient.searchBooksSimple(keyword);
    }
}
