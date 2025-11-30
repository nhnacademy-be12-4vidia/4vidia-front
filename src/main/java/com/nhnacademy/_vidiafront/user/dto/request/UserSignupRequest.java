package com.nhnacademy._vidiafront.user.dto.request;

import java.time.LocalDate;


// 회원가입 요청 DTO
public record UserSignupRequest(
        String email,
        String password,
        String name,
        String phone,
        LocalDate birthDate
) {

}
