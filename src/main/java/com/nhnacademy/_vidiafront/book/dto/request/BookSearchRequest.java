package com.nhnacademy._vidiafront.book.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookSearchRequest(
    String keyword,
    String sort,
    Long categoryId,
    Integer minPrice,
    Integer maxPrice,
    Boolean useSemantic
) {

}
