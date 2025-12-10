package com.nhnacademy._vidiafront.global.config;

import com.nhnacademy._vidiafront.global.interceptor.TokenInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    @Primary
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("lb://4vidia-gateway") // lb:// 프로토콜 사용 가능
                .requestInterceptor(new TokenInterceptor())
                .build();
    }
    @Bean
    public RestClient externalRestClient() {
        // 여기서 주입받은 builder를 쓰면 안됩니다!
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory()) // 필요하다면 팩토리 설정 추가
            .build();
    }
}
