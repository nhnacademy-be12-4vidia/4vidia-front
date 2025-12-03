package com.nhnacademy._vidiafront.user.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}