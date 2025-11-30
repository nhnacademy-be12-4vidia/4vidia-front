package com.nhnacademy._vidiafront.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        // 모든 템플릿에서 ${currentUri}로 접근 가능
        return request.getRequestURI(); 
    }
}