package com.nhnacademy._vidiafront.user.dto.user.response;

import com.nhnacademy._vidiafront.user.dto.address.response.AddressResponse;

import java.time.LocalDate;

public record UserProfileResponse(
        Long userId,
        String email,
        String name,
        String phone,
        LocalDate birthDate,
        int point,
        AddressResponse defaultAddress,
        String gradeName
) {

}
