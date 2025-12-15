package com.nhnacademy._vidiafront.admin.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminBookCreateRequest (
        String isbn,
        String title,
        String subtitle,
        String authors,       // 쉼표(,)로 구분된 문자열
        String publisher,     // 출판사 이름

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate publishedDate,

        String language,
        Integer pageCount,
        String categoryCode,  // 카테고리 이름

        BigDecimal priceStandard,
        BigDecimal priceSales,
        Integer stock,
        Boolean packagingAvailable,

        String description,
        String bookIndex,
        String tags,          // 쉼표(,)로 구분된 문자열
        String imageUrl
){}
