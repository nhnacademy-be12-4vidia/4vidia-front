package com.nhnacademy._vidiafront.admin.dto.request;

import org.springframework.util.StringUtils;

public record AdminBookSearchRequest(
    Integer page,
    Integer size,
    String isbn,
    String title
) {
    public Integer pageOrDefault() {
        return page == null ? 0 : page;
    }

    public Integer sizeOrDefault() {
        return size == null ? 10 : size;
    }

    public String isbnOrNull() {
        return StringUtils.hasText(isbn) ? isbn : null;
    }

    public String titleOrNull() {
        return StringUtils.hasText(title) ? title : null;
    }
}
