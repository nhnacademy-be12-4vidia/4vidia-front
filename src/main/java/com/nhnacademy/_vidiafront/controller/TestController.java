package com.nhnacademy._vidiafront.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class TestController {

    private final WebClient webClient;

    public TestController() {
        this.webClient = WebClient.builder()
                .baseUrl("https://www.4vidia.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping("/test")
    @ResponseBody
    public Mono<String> test() {
        return webClient.get()
                .uri("/api/v1/coupon/test")
                .retrieve()
                .bodyToMono(String.class);

    }

    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html
    }
}
