package com.nhnacademy._vidiafront.admin.dto.request;

public record AdminReviewSearchRequest(
        String keyword,
        Integer rating,
        Integer page,
        Integer size
) {

    public String keywordOrNull() {
        if (keyword == null) return null;
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public Integer ratingOrNull() {
        return rating;
    }
    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 20 : size;
    }
}