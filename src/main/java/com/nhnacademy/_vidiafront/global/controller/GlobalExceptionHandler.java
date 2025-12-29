package com.nhnacademy._vidiafront.global.controller;

import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {




    @ExceptionHandler(ApiRequestException.class)
    public String handleApiRequestException(
            ApiRequestException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        log.warn("API error: status={}, errorCode={}, message={}",
                e.getStatus(), e.getErrorCode(), e.getMessage());

        // ===== 인증 관련 =====
        if (e.getStatus() == 401) {
            return "redirect:/auth/login";
        }

        // ===== 권한 관련 =====
        if (e.getStatus() == 403) {
            if ("TEMP_USER".equals(e.getErrorCode())) {
                return "redirect:/complete-profile";
            }
            if ("DORMANT_USER".equals(e.getErrorCode())) {
                return "redirect:/auth/dormant-auth";
            }
            if ("DELETED_USER".equals(e.getErrorCode())) {
                redirectAttributes.addFlashAttribute(
                        "alertMessage",
                        "이미 탈퇴 처리된 계정입니다."
                );
                return "redirect:/auth/login";
            }
        }

        // ===== 그 외 에러 페이지 =====
        request.setAttribute("errorMessage", e.getMessage());
        return "error/errorPage";
    }


    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public String handleUnauthorizedException(HttpClientErrorException.Unauthorized ex, HttpServletResponse response) {
        // 🚨 로그아웃 처리가 필요한 경우 여기에 추가 로직을 넣을 수 있습니다.
        // 예: 세션 무효화 또는 쿠키 삭제 (현재는 BackendApiClient에서 토큰 재발급 후 실패 시 이리로 오므로 필요 없을 수도 있음)

        // 최종 사용자(브라우저)에게 리다이렉트 명령을 내립니다.
        log.info(ex.getMessage());
        return "redirect:/auth/login";
    }


    /** 백엔드에서 ErrorResponse(ProblemDetail) 와 함께 전달되는 4xx 예외 처리 */
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public String handleConflict(HttpClientErrorException.Conflict ex, Model model) {

        log.error("409 Conflict 발생: {}", ex.getResponseBodyAsString());

        // ProblemDetail JSON 파싱
        String message = extractMessage(ex.getResponseBodyAsString());

        model.addAttribute("errorMessage", message);

        // 원하는 에러 페이지로 이동
        return "error/errorPage";
    }



    /** ProblemDetail JSON에서 detail 메시지만 추출하는 헬퍼 */
    private String extractMessage(String responseBody) {
        try {
            // JSON을 파싱할 수 있는 형태로 처리 (ObjectMapper 사용)
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var json = mapper.readTree(responseBody);

            if (json.has("detail")) {
                return json.get("detail").asText();
            }
        } catch (Exception ignore) {}

        return "알 수 없는 오류가 발생했습니다.";
    }


    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public String handleForbidden(HttpClientErrorException ex, HttpServletResponse response, Model model) {

        String errorCode = ex.getResponseHeaders()
                .getFirst("X-Error-Code");

        if ("TEMP_USER".equals(errorCode)) {
            return "redirect:/complete-profile";
        }

        if ("DORMANT_USER".equals(errorCode)) {
            return "redirect:/auth/dormant-auth";
        }
        String message = extractMessage(ex.getResponseBodyAsString());
        model.addAttribute("errorMessage", message);

        return "error/errorPage";
    }

    @ExceptionHandler(IOException.class)
    public String handleRedirectException(IOException e) {
        if (e.getMessage().contains("Redirecting TEMP user")) {
            // TEMP 사용자 리다이렉트 관련 예외는 이미 응답이 커밋되었으므로, null을 반환하여 뷰 렌더링을 중단
            log.debug("Handled TEMP user redirect exception: {}", e.getMessage());
            return null;
        }
        // 그 외 IOException은 기본 에러 페이지로 처리
        log.error("General IOException occurred: {}", e.getMessage());
        return "error/errorPage";
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(
                        500,
                        "서버 오류가 발생했습니다.",
                        "INTERNAL_SERVER_ERROR"
                ));
    }
}