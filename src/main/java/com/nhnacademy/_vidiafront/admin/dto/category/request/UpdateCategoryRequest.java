package com.nhnacademy._vidiafront.admin.dto.category.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    String categoryName
) {}
