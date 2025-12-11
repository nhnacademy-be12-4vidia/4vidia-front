package com.nhnacademy._vidiafront.book.dto.books.response;

import java.util.List;

public record BookListResponse(
        Long id,
        String title,
        String isbn,
        Integer priceStandard, // 정가 추가
        Integer priceSales,
        List<String> authorNames,
        String publisherName,
        String imageUrl,
        boolean liked,
        Integer rank,
        Double relevanceScore,
        boolean recommended,
        String llmSummary
) {

}
