package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.user.dto.like.response.LikeResponse;
import com.nhnacademy._vidiafront.user.dto.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeApiClient {
    private final RestClient restClient;

    private static final String USER_SERVICE = "/api/v1/user-service";

    // 테스트용
    private static final int TEST_ID = 1;
    private static final String X_USER_ID = "X-User-Id";


    /**
     * GET 좋아요 리스트 조회
     * */
    public List<LikeResponse> getLikeList() {
        return restClient.get()
                .uri(USER_SERVICE + "/my/likes")
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * POST 좋아요 등록
     */
    public void addLike(Long bookId) {
        restClient.post()
                .uri(USER_SERVICE + "/my/likes/" + bookId)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * DELETE 좋아요 삭제
     */
    public void deleteLike(Long bookId) {
        restClient.delete()
                .uri(USER_SERVICE + "/my/likes/" + bookId)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
    }


}
