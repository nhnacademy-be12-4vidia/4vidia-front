package com.nhnacademy._vidiafront.book.dto.reviews.response;

import com.nhnacademy._vidiafront.book.dto.books.response.ReviewListResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;

public record ReviewListWithSummaryResponse(
        PageResponse<ReviewListResponse> reviews,
        String reviewSummary
) {
}
