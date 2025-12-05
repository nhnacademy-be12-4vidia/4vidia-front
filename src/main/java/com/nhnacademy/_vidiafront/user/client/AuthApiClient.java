package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindIdRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindPasswordRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.LoginRequest;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AuthApiClient {
    private final BackendApiClient backendApiClient;

    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String AUTH = "/api/v1/auth";

    /**
     * POST 회원가입
     *
     */
    public Long signup(UserSignupRequest userSignupRequest) {
        return backendApiClient.post(USER_SERVICE + "/auth/signup", userSignupRequest, Long.class);
    }

    /**
     * 회원 아이디(email) 찾기
     */
    public String findUserId(FindIdRequest findIdRequest) {
        return backendApiClient.post(USER_SERVICE + "/auth/find-id", findIdRequest, String.class);
    }

    /**
     * 회원 비밀번호 찾기
     */
    public String findUserPassword(FindPasswordRequest findPasswordRequest) {
        return backendApiClient.post(USER_SERVICE + "/auth/find-password", findPasswordRequest, String.class);
    }

    /**
     * post 로 로그인
     */
    public TokenResponse login(LoginRequest loginRequest) {
        return backendApiClient.post(AUTH + "/auth/login", loginRequest, TokenResponse.class);
    }



    /**
     * 이메일 중복여부
     */
    public String existsByEmail(String email) {
        return backendApiClient.get(USER_SERVICE + "/auth/check-email?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8), String.class);
    }
}
