package com.nhnacademy._vidiafront.controller;

import com.nhnacademy._vidiafront.dto.TestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class TestController {

    private final WebClient webClient;

    public TestController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://4vidia-gateway-1:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // 최초 페이지 렌더링
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html
    }

    // 버튼 클릭 시 서버에서 호출
    @GetMapping("/test")
    public String test(Model model) {
        TestResponse block = webClient.get()
                .uri("/api/v1/coupon/test")
                .retrieve()
                .bodyToMono(TestResponse.class)
                .block();


        model.addAttribute("testResponse", block);
        return "index"; // 동일 index.html 렌더링

    }
}
