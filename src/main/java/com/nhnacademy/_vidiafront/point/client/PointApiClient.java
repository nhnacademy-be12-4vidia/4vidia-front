package com.nhnacademy._vidiafront.point.client;



import com.nhnacademy._vidiafront.point.dto.response.PointHistoryPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor

public class PointApiClient {
    private final RestClient restClient;

    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final int TEST_ID = 1;
    private static final String X_USER_ID = "X-User-Id";

    // 현재 보유 포인트 조회
    public Integer getRemain() {
        return restClient.get()
                .uri(USER_SERVICE + "/points/remain")
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(Integer.class);
    }

    // 소멸 예정 포인트 조회 (기본 30일)
    public Integer getExpireSoon(int days) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_SERVICE + "/points/expire-soon")
                        .queryParam("days", days)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(Integer.class);
    }

    // 포인트 내역 조회 (페이징)
    public PointHistoryPageResponse getHistory(int page, int size) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_SERVICE + "/points/history")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(PointHistoryPageResponse.class);   // Page DTO 별도 생성 필요
    }

}
