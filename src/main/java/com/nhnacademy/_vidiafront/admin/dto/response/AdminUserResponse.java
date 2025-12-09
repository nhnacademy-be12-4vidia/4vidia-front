package com.nhnacademy._vidiafront.admin.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
public record AdminUserResponse(
        Long userId,
        String email,
        String name,
        String phone,
        LocalDate birthDate,
        Integer point,
        String status,
        LocalDate joinedAt,      // ← 여기 LocalDateTime
        LocalDateTime lastLoginAt,
        String gradeName
) { }

