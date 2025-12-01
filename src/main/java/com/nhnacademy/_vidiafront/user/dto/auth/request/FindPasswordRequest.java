package com.nhnacademy._vidiafront.user.dto.auth.request;

// 비밀번호 찾기 요청 dto
public record FindPasswordRequest(
    String email,
    String name,
    String phone
){

}
