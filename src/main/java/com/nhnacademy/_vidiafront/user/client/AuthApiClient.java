package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindIdRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.FindPasswordRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.LoginRequest;
import com.nhnacademy._vidiafront.user.dto.auth.request.PaycoCodeRequest;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateLastLoginRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String BASE_URL = "/auth";

    private static final String AUTH = "/api/v1/auth";

    /**
     * 회원가입
     */
    public Long signup(UserSignupRequest userSignupRequest) {
        return backendApiClient.post(USER_SERVICE + BASE_URL + "/signup", userSignupRequest,new ParameterizedTypeReference<>() {});
    }

    /**
     * 회원 아이디(email) 찾기
     */
    public String findUserId(FindIdRequest findIdRequest) {
        return backendApiClient.post(USER_SERVICE + BASE_URL + "/find-id", findIdRequest, new ParameterizedTypeReference<>() {});
    }

    /**
     * 회원 비밀번호 새로 발급
     * 기존 "/auth/find-password"
     * 기존 findUserPassword(...)
     */
    public void issueNewPassword(FindPasswordRequest findPasswordRequest) {
        backendApiClient.post(USER_SERVICE + BASE_URL + "/reset-password", findPasswordRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 이메일 중복여부
     * 기존 "/auth/check-email?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
     */
    public String existsByEmail(String email) {
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/emails/exists?email=" + encodedEmail, new ParameterizedTypeReference<>() {});
    }

    /**
     * 로그인 후 -> 휴먼 여부 확인
     * 기존 "/auth/check-dormant?email=" + email
     */
    public Boolean isDormant(String email) {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/dormant?email=" + email, new ParameterizedTypeReference<>() {});
    }

    /**
     * 휴면 인증코드 전송
     */
    public void sendDormantCode(String email, String webhookUrl) {
        backendApiClient.post(
                USER_SERVICE + BASE_URL + "/dormant/send-code",
                java.util.Map.of("email", email, "webhookUrl", webhookUrl),
                new ParameterizedTypeReference<>() {}        );
    }

    /**
     * 휴면 인증코드 검증
     */
    public void verifyDormantCode(String email, String code) {
        backendApiClient.post(
                USER_SERVICE + BASE_URL + "/dormant/verify",
                java.util.Map.of("email", email, "code", code),
                new ParameterizedTypeReference<ApiResponse<Void>>() {}
        );
    }

    /**
     * 메일로 휴면 인증코드 전송
     */
    public void sendDormantCodeByEmail(String email, String contactEmail) {
        backendApiClient.post(
                USER_SERVICE + BASE_URL + "/dormant/send-code/email",
                Map.of("email", email, "contactEmail", contactEmail),
                new ParameterizedTypeReference<ApiResponse<Void>>() {}
        );
    }
    /**
     * 회원가입 이메일 인증코드 발송
     */
    public void sendSignupEmailCode(String email){
        backendApiClient.post(
                USER_SERVICE+BASE_URL+"/email/send-code",
                Map.of("email", email),
                new ParameterizedTypeReference<ApiResponse<Void>>() {}
        );
    }
    /**
     * 회원가입 이메일 인증코드 검증
     */
    public void verifySignupEmailCode(String email, String code){
        backendApiClient.post(
                USER_SERVICE+BASE_URL+"/email/verify-code",
                Map.of("email",email,"code",code),
                new ParameterizedTypeReference<>() {}
        );
    }


    /**
     * 마지막로그인시간 업데이트하기
     * 기존 "/auth/update-time"
     */
    public void updateLastLoginAt(String email) {
        UpdateLastLoginRequest updateLastLoginRequest = new UpdateLastLoginRequest(email);
        backendApiClient.put(USER_SERVICE + BASE_URL + "/last-login", updateLastLoginRequest, new ParameterizedTypeReference<>() {});
    }


    // todo : /api/v1/auth/auth /login or /logout 인데 맞아요?? auth 두 번???
    /**
     * 로그인
     */
    public TokenResponse login(LoginRequest loginRequest) {
        return backendApiClient.post(AUTH + "/auth/login", loginRequest, new ParameterizedTypeReference<>() {});
    }

    /**
     * payco Token callback
     */
    public TokenResponse paycoCallback(PaycoCodeRequest paycoCodeRequest) {
        return backendApiClient.post(AUTH + "/login/oauth2/code/payco", paycoCodeRequest, new ParameterizedTypeReference<>() {});
    }

    /**
     * 로그아웃
     */
    public String logout() {
        return backendApiClient.postNoBody(AUTH + "/auth/logout", new ParameterizedTypeReference<>() {});
    }

    /**
     *
     * access토큰 재발급
     */
    public TokenResponse reissueToken(String refreshUuid) {
        return backendApiClient.post(AUTH + "/auth/reissue", refreshUuid , new ParameterizedTypeReference<>() {});
    }

    public void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경이면 true
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }




}
