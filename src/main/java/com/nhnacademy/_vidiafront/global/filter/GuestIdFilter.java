package com.nhnacademy._vidiafront.global.filter;

import com.nhnacademy._vidiafront.global.SnowflakeIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class GuestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        boolean exists = false;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("guest_id")) {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            String guestId = String.valueOf(SnowflakeIdGenerator.nextId());

            Cookie cookie = new Cookie("guest_id", guestId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 3); // 3일
            cookie.setHttpOnly(false); // JS 필요없으면 true 해도됨
            cookie.setSecure(false);   // HTTPS면 true
            response.addCookie(cookie);
            log.info("[FRONT] guest_id 생성: {}",guestId);
        }
        filterChain.doFilter(request, response);
    }
}
