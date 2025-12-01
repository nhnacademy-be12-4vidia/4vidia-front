package com.nhnacademy._vidiafront.user.dto.user.response;

// user-service -> auth
public record UserInfoResponse(
        Long id,
        String email,
        String password,
        String roles        // "ROLE_USER", "ROLE_ADMIN"
) {
}