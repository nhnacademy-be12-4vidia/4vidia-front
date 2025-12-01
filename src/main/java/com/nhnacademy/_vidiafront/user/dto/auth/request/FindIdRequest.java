package com.nhnacademy._vidiafront.user.dto.auth.request;

// 아이디 찾기 요청 dto
public record FindIdRequest (
    String name,
    String birthday,
    String phone
) {
}

