package com.nhnacademy._vidiafront.admin.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AdminReviewResponse(
        Long id,
        Long bookId,
        String bookTitle,
        String email,
        String userNickname,
        Integer rating,
        String content,
        boolean hasPhoto,
        List<String> imageUrls,
        LocalDate createdAt
) { }