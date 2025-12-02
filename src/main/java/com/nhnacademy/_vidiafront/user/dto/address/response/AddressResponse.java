package com.nhnacademy._vidiafront.user.dto.address.response;

public record AddressResponse(
    Long addressId,
    String alias,
    String roadAddress,
    String zipCode,
    String addressDetail
) { }
