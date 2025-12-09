package com.nhnacademy._vidiafront.admin.dto.request;

public record UpdateUserStatusRequest(
        String status   // "ACTIVE" / "DORMANT" / "DELETED"
) { }
