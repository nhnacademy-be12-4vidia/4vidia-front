package com.nhnacademy._vidiafront.admin.dto.request;

import com.nhnacademy._vidiafront.book.dto.authors.request.AuthorRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AdminBookCreateRequest (

        String coverImageUrl,
        String isbn,
        String title,
        String subtitle,
        List<AuthorRequest> authorList,
        String publisher,     // 출판사 이름

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate publishedDate,

        String language,
        Integer pageCount,
        String categoryCode,

        Integer priceStandard,
        Integer priceSales,
        Integer stock,
        Boolean packagingAvailable,

        String description,
        String bookIndex,
        List<String> tags
){}
