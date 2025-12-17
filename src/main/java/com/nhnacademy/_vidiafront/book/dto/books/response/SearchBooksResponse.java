package com.nhnacademy._vidiafront.book.dto.books.response;

import com.nhnacademy._vidiafront.global.dto.PageResponse;

import java.util.List;

public record SearchBooksResponse(
        PageResponse<BookListResponse> page,
        List<AiCacheResponse> aiCacheResponseList
) {}
