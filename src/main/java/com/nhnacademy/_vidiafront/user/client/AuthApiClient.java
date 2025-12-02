package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.user.dto.auth.request.FindIdRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindPasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AuthApiClient {
    private final RestClient restClient;

    private static final String USER_SERVICE = "/api/v1/user-service";

    /**
     * POST 회원가입
     * */
    public Void signup(UserSignupRequest userSignupRequest) {
        return restClient.post()
                .uri(USER_SERVICE + "/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userSignupRequest)
                .retrieve()
                .body(Void.class);
    }

    /**
     * 회원 아이디(email) 찾기
     */
    public String findUserId(FindIdRequest findIdRequest) {
        return restClient.post()
                .uri("/auth/find-id")
                .contentType(MediaType.APPLICATION_JSON)
                .body(findIdRequest)
                .retrieve()
                .body(String.class);
    }

    /**
     * 회원 비밀번호 찾기
     */
    public String findUserPassword(FindPasswordRequest findPasswordRequest) {
        return restClient.post()
                .uri("/auth/find-password")
                .contentType(MediaType.APPLICATION_JSON)
                .body(findPasswordRequest)
                .retrieve()
                .body(String.class);
    }

}
