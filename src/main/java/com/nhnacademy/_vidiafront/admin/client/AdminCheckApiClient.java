package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCheckApiClient {

    private static final String USER_SERVICE = "/api/v1/user-service";
    private final BackendApiClient backendApiClient;

    public String checkAdmin() {
        return backendApiClient.get(USER_SERVICE + "/admin" + "/users/name", new ParameterizedTypeReference<>() {});
    }
}
