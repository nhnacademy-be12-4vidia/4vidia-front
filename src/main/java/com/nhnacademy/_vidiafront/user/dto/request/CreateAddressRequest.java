package com.nhnacademy._vidiafront.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 주소 등록 DTO
public record CreateAddressRequest(
        String alias,// 별칭
        String roadAddress,//도로명 주소
        String zipCode, // 우편번호
        String addressDetail
){

}
