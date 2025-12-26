package com.nhnacademy._vidiafront.admin.dto.request;

import com.nhnacademy._vidiafront.book.dto.authors.request.AuthorRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record AdminBookUpdateRequest(

        String thumbnailUrl,
        String isbn,
        String title,
        String subtitle,
        List<AuthorRequest> authorList,
        String publisherName,     // 출판사 이름

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate publishedDate,

        String language,
        Integer pageCount,
        Long categoryId,

        Integer priceStandard,
        Integer stock,
        String stockStatus,
        Boolean packagingAvailable,

        String description,
        String bookIndex,
        String tagList

) {
}
