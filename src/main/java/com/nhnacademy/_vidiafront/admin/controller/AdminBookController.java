package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminBookApiClient;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminIsbnSearchResponse;
import com.nhnacademy._vidiafront.book.client.BookApiClient;
import com.nhnacademy._vidiafront.book.client.CategoryApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookController {

    private final AdminBookApiClient adminBookApiClient;
    private final BookApiClient bookApiClient;
    private final CategoryApiClient categoryApiClient;

    // 도서 생성 폼
    @GetMapping
    public String getBookCreateForm(
            Model model
    ) {
        model.addAttribute("mode", "create");
        model.addAttribute("categories", categoryApiClient.getFlatCategoryList());
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
    @GetMapping("/{book-id}")
    public String getBookUpdateForm(
            @PathVariable("book-id") Long bookId,
            Model model
    ) {
        String isbn = adminBookApiClient.getBookIsbn(bookId);
        AdminIsbnSearchResponse adminData = adminBookApiClient.searchBookByIsbn(isbn);

        model.addAttribute("book", adminData);
        model.addAttribute("mode", "update");
        model.addAttribute("categories", categoryApiClient.getFlatCategoryList());
        return "admin/admin-book-form";
    }

    // 도서 수정 요청
    @PutMapping("/{book-id}")
    public String updateBook(
            @PathVariable("book-id") Long bookId,
            AdminBookUpdateRequest request
    ) {
        adminBookApiClient.updateBook(bookId, request);
        return "redirect:/admin/books";
    }

    // [AJAX] ISBN 검색
    @ResponseBody
    @GetMapping("/search")
    public ResponseEntity<AdminIsbnSearchResponse> searchBook(
            @RequestParam String isbn
    ) {
        AdminIsbnSearchResponse response = adminBookApiClient.searchBookByIsbn(isbn);
        return ResponseEntity.ok(response);
    }

    // [AJAX] 도서 정보 보강 (수정 모드용)
    @ResponseBody
    @GetMapping("/augment")
    public ResponseEntity<AdminIsbnSearchResponse> augmentBook(
            @RequestParam String isbn
    ) {
        AdminIsbnSearchResponse response = adminBookApiClient.getAugmentedBookInfo(isbn);
        return ResponseEntity.ok(response);
    }

    // [AJAX] 이미지 업로드
    @ResponseBody
    @PostMapping("/images")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("image") MultipartFile image
    ) {
        try {
            Map<String, String> response = adminBookApiClient.uploadImage(image);
            if (response == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Image upload failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

