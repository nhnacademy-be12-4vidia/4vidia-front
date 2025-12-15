package com.nhnacademy._vidiafront.admin.dto.response;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public record AdminIsbnSearchResponse(

        String coverImageUrl,
        String title,
        String subtitle,
        List<BookAuthorResponse> authors,
        String publisher,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String pubDate,
        String language,
        Integer pageCount,
        String categoryCode,

        Integer priceStandard,
        Integer stock,

        String description,

        String bookIndex,
        List<String> tags
) {
    public record BookAuthorResponse(
            String name,
            String role
    ) {
    }
}
