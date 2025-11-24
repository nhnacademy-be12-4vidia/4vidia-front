package com.nhnacademy._vidiafront.controller;

import com.nhnacademy._vidiafront.dto.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
public class TestController {

    private final RestTemplate restTemplate;

    public TestController() {
        this.restTemplate = new RestTemplate();
    }

    // 최초 페이지 렌더링
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html
    }

    // 버튼 클릭 시 서버에서 호출
    @GetMapping("/test")
    public String test(Model model) {
        // RestTemplate으로 GET 요청
        TestResponse response = restTemplate.getForObject(
                "http://4vidia-gateway-1:8080/api/v1/coupon/test",
                TestResponse.class
        );
        log.info(response.toString());
        model.addAttribute("testResponse", response);
        return "hi"; // 동일 index.html 렌더링
    }
}
