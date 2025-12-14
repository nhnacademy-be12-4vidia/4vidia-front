package com.nhnacademy._vidiafront.user.dto.auth.request;

import java.time.LocalDate;

public record CompleteProfileRequest(
        String email,
        String name,
        String phone,
        LocalDate birthDate
) {
}