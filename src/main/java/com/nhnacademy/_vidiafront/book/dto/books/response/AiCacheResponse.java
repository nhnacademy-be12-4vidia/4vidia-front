package com.nhnacademy._vidiafront.book.dto.books.response;

import java.util.List;

public record AiCacheResponse(
        Long id,
        String title,
        String isbn,
        Integer priceStandard,
        Integer priceSales,
        List<String> authorNames,
        String publisherName,
        String imageUrl,
        Integer rank,
        Double relevanceScore,
        boolean recommended,
        String llmSummary
) {}

