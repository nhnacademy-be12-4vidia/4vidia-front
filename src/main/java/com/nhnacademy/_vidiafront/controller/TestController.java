package com.nhnacademy._vidiafront.controller;

import com.nhnacademy._vidiafront.dto.TestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {


    private final RestClient restClient;


    // 최초 페이지 렌더링
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html
    }

    // 버튼 클릭 시 서버에서 호출
    @GetMapping("/test")
    public String test(Model model) {
        TestResponse response = restClient.get() // GET 요청 시작
                .uri("/api/v1/coupon/test") // URI 설정
                .retrieve() // 응답 검색
                .body(TestResponse.class); // 응답 본문을 TestResponse 클래스로 역직렬화

        model.addAttribute("testResponse", response);
        return "hi"; // 동일 index.html 렌더링
    }
}
