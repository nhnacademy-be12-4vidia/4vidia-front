package com.nhnacademy._vidiafront.global.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * layout.html에서 사용 (ex 마이페이지 누르면 열리는 목록 고정시키기?)
     * */
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        // 모든 템플릿에서 ${currentUri}로 접근 가능
        return request.getRequestURI(); 
    }
}