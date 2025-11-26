package com.nhnacademy._vidiafront.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory());
    }



    // 2단계: 위에서 만든 빌더를 주입받아 구체적인 설정(baseUrl 등)을 추가하여 RestClient 생성
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("lb://4vidia-gateway") // lb:// 프로토콜 사용 가능
                .build();
    }
}
