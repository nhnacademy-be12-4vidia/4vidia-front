package com.nhnacademy._vidiafront.user.dto.response;

import java.time.LocalDate;

public record UserProfileResponse(
        Long userId,
        String email,
        String name,
        String phone,
        LocalDate birthDate,
        int point, // todo : Integer로 변경해야함
        AddressResponse defaultAddress,
        String gradeName
) {

}
