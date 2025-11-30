package com.nhnacademy._vidiafront.user.dto.request;


import jakarta.validation.constraints.NotBlank;

/**
 * 비밀번호 변경 dto
 */
public record ChangePasswordRequest (
        String currentPassword,
        String newPassword,
        String confirmPassword
){

}
