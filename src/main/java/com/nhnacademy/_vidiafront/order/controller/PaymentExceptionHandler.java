package com.nhnacademy._vidiafront.order.controller;

import com.nhnacademy._vidiafront.order.exception.ApiPaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(ApiPaymentException.class)
    public String handleApiClientError(ApiPaymentException e) {

        log.error("API 결제 중 에러 발생: {}", e.getMessage());

        return "redirect:/orders/fail?message=" + e.getMessage();
    }
}