package com.nhnacademy._vidiafront.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.csrfTokenRepository(
                        CookieCsrfTokenRepository.withHttpOnlyFalse()

                )
                        .ignoringRequestMatchers(
                                "/auth/login",
                                "/auth/logout",
                                "/login/oauth2/**",
                                "/mypage/address/jusoCallback",
                                "/mypage/address"
                        ));
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .logout(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );


        return http.build();

    }


    @Bean
    @Profile({"local", "test"})
    public CookieSerializer localCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("Lax"); // 기본값
        serializer.setUseSecureCookie(false);
        return serializer;
    }


}