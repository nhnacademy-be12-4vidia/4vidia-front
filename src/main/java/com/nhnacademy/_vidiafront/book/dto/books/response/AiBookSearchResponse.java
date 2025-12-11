package com.nhnacademy._vidiafront.book.dto.books.response;

import com.nhnacademy._vidiafront.global.dto.PageResponse;

public record AiBookSearchResponse(PageResponse<BookListResponse> results,
                                   String aiAnswer) {

}
