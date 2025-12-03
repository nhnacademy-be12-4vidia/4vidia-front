package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.like.response.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";

    // 테스트용
    private static final int TEST_ID = 1;
    private static final String X_USER_ID = "X-User-Id";


    /**
     * GET 좋아요 리스트 조회
     * */
    public List<LikeResponse> getLikeList() {
        return backendApiClient.get(USER_SERVICE + "/my/likes", new ParameterizedTypeReference<>() {});
    }

    /**
     * POST 좋아요 등록
     */
    public Void addLike(Long bookId) {
        return backendApiClient.postNoBody(USER_SERVICE + "/my/likes", Void.class);
    }

    /**
     * DELETE 좋아요 삭제
     */
    public Void deleteLike(Long bookId) {
        return backendApiClient.delete(USER_SERVICE + "/my/likes/" + bookId, Void.class);
    }


}
