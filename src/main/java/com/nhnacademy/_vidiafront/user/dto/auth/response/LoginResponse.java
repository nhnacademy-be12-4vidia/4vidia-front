package com.nhnacademy._vidiafront.user.dto.auth.response;


// 로그인 응답
public record LoginResponse(
        String email,
        String password // password는 넘어가면 안되는데..
) {
}
