package com.nhnacademy._vidiafront.user.dto.user.response;

import com.nhnacademy._vidiafront.user.dto.address.response.AddressResponse;

import java.util.List;

public record OrderUserResponse(
        String email,
        String name,
        String phone,
        Integer point,
        Long addressId,
        String alias,
        String roadAddress,
        String zipCode,
        String addressDetail,
        List<AddressResponse> addressResponses
) {
    public record AddressResponse(
            Long addressId,
            String alias,
            String roadAddress,
            String zipCode,
            String addressDetail
    ) { }
}
