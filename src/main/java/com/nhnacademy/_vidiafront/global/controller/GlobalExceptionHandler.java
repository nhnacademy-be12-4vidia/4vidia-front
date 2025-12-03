package com.nhnacademy._vidiafront.global.controller;

import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public String handleApiClientError(ApiRequestException e, Model model) {

        log.error("API 호출 중 에러 발생: {}", e.getMessage());

        model.addAttribute("errorMessage", e.getMessage());

        return "error/errorPage";
    }
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public String handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) {
        // 🚨 로그아웃 처리가 필요한 경우 여기에 추가 로직을 넣을 수 있습니다.
        // 예: 세션 무효화 또는 쿠키 삭제 (현재는 BackendApiClient에서 토큰 재발급 후 실패 시 이리로 오므로 필요 없을 수도 있음)

        // 최종 사용자(브라우저)에게 리다이렉트 명령을 내립니다.
        return "redirect:/auth/login";
    }
}