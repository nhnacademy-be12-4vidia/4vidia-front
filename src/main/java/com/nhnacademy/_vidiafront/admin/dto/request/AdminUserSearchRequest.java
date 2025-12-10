package com.nhnacademy._vidiafront.admin.dto.request;

public record AdminUserSearchRequest(
        String keyword,
        String status,
        Integer page,
        Integer size
) {
    public String keywordOrNull() {
        if (keyword == null) return null;
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }


    public String statusOrNull(){
        return (status != null && !status.isBlank()) ? status : null;
    }
    public int pageOrDefault(){
        return page == null ? 0 : page;
    }
    public int sizeOrDefault(){
        return size == null ? 20: size;
    }
}
