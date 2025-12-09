package com.nhnacademy._vidiafront.global.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class TokenInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // 1. 현재 프론트 서버로 들어온 요청(HttpServletRequest) 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest incomingRequest = attributes.getRequest();

            // 2. HttpSession에서 Access Token 가져오기
            HttpSession session = incomingRequest.getSession(false); // 세션 없으면 null
            if (session != null) {
                String accessToken = (String) session.getAttribute("accessToken");

                // 3. Access Token이 있으면 Authorization 헤더에 추가
                if (accessToken != null) {
                    request.getHeaders().set("Authorization", "Bearer " + accessToken);
                }
            }
            // 토큰 없으면(비회원) 아무것도 안 하고 통과 -> 백엔드가 비회원 처리
        }

        return execution.execute(request, body);
    }
}
