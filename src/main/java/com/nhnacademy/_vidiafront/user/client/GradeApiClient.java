package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.grade.response.GradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GradeApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String BASE_URL = "/users/me/grade";
    // 기존 base url = "/my/grades" -> 수정 "/users/me/grade"

    /**
     * 등급 조회
     * 기존 "/my/grade"
     */
    public GradeResponse getGrade() {
        return backendApiClient.get(USER_SERVICE + BASE_URL, new ParameterizedTypeReference<>() {});
    }

    /**
     * 등급 변경
     * 기존 "/my/grade"
     */
    public String updateGrade(Long gradeId) {
        return backendApiClient.putNoBody(USER_SERVICE + BASE_URL + "/" + gradeId, new ParameterizedTypeReference<>() {});
    }

}
