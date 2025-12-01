package com.nhnacademy._vidiafront.user.dto.auth.request;

// 로그인 요청
public record LoginRequest(
        String email,
        String password
) {
}
