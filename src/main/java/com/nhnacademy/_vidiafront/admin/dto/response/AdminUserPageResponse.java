package com.nhnacademy._vidiafront.admin.dto.response;

import java.util.List;

public record AdminUserPageResponse(
        List<AdminUserResponse> content,
        int number,
        int size,
        int totalPages,
        long totalElements,
        boolean first,
        boolean last
) {
}
