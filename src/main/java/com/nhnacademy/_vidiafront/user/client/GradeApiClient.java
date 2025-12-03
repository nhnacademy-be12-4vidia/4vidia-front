package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.grade.response.GradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GradeApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";

    /**
     * 등급 조회
     */
    public GradeResponse getGrade() {
        return backendApiClient.get(USER_SERVICE + "/my/grade", GradeResponse.class);
    }

    /**
     * 등급 변경
     */
    public String updateGrade(Long gradeId) {
        return backendApiClient.putNoBody(USER_SERVICE + "/my/grade/" + gradeId, String.class);
    }

}
