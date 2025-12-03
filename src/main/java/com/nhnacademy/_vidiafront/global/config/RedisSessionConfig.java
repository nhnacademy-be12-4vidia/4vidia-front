package com.nhnacademy._vidiafront.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
    // 별도 Bean 설정 필요 없음
}