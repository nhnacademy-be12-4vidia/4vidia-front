package com.nhnacademy._vidiafront.user.dto.user.request;

/**
 * 유저 정보 수정용 dto
 */
public record UpdateUserRequest (
        String name,
        String phone
){

}
