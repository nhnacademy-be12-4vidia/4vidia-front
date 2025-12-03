package com.nhnacademy._vidiafront.point.client;



import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.point.dto.response.PointExpireSoon;
import com.nhnacademy._vidiafront.point.dto.response.PointHistoryPageResponse;
import com.nhnacademy._vidiafront.point.dto.response.PointTotalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor

public class PointApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";

    /**
     * 현재 보유 포인트 조회
     */
    public PointTotalResponse getPointTotal() {
        return backendApiClient.get(
                USER_SERVICE+"/points/remain",PointTotalResponse.class
        );
    }

    /**
     * 소멸 예정 포인트 조회 ( 기본 7일 )
     */
    public PointExpireSoon getExpireSoon(int days){
        return backendApiClient.get(
                USER_SERVICE+"/points/expire-soon?days="+days,
                PointExpireSoon.class
        );
    }


    /**
     * 포인트 내역 조회
     */
    public PointHistoryPageResponse getHistoryPage(int page, int limit) {
        return backendApiClient.get(
                USER_SERVICE+"/points/history?page="+page+"&size="+limit,
                PointHistoryPageResponse.class
        );
    }

}
