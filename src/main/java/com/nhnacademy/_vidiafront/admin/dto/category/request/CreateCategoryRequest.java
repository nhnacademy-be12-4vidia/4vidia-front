package com.nhnacademy._vidiafront.admin.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCategoryRequest(
    @NotBlank(message = "KDC 코드를 입력해주세요.")
    @Pattern(regexp = "^[A-Z0-9]{3}$", message = "KDC 코드는 3자리의 숫자 또는 대문자여야 합니다.")
    String kdcCode,

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    String categoryName
) {}