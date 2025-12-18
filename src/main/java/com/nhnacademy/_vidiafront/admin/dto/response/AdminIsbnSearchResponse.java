package com.nhnacademy._vidiafront.admin.dto.response;

import com.nhnacademy._vidiafront.book.dto.authors.response.AuthorResponse;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public record AdminIsbnSearchResponse(

        String coverImageUrl,
        String title,
        String subtitle,
        List<AuthorResponse> authors,
        String publisher,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String publishedDate,

        String language,
        Integer pageCount,
        String categoryCode,

        Integer priceStandard,
        //Integer stock,
        //Boolean packagingAvailable,

        String description,
        String bookIndex,
        List<String> tags
) {
}
