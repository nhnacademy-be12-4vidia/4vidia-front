package com.nhnacademy._vidiafront.book.controller;

import com.nhnacademy._vidiafront.book.client.CategoryApiClient;
import com.nhnacademy._vidiafront.book.dto.categories.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryApiClient categoryApiClient;

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryApiClient.getCategoryList();
    }
}
