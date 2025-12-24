package com.nhnacademy._vidiafront.book.dto.books.request;

import jakarta.validation.constraints.NotBlank;

public record BookSearchRequest(
    String keyword,
    BookSortOptions sort,
    Long categoryId,
    Integer minPrice,
    Integer maxPrice,
    Boolean useSemantic
) {

}
