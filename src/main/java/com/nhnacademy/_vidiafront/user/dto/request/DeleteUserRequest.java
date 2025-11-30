package com.nhnacademy._vidiafront.user.dto.request;

/**
 * 회원 탈퇴 시 비밀번호 확인 dto
 */
public record DeleteUserRequest( 
    String currentPassword
) {

}