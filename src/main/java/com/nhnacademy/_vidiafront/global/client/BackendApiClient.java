package com.nhnacademy._vidiafront.global.client;

import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class BackendApiClient {
    private final RestClient restClient;
    private final String AUTH = "/api/v1/auth";

    // ------------------- GET -------------------
    public <T> T get(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.get()
                .uri(URI.create(uri))
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .retrieve()
                .body(responseType);

    }

    public <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);


        return restClient.get()
                .uri(URI.create(uri))
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .retrieve()
                .body(typeReference);

    }

    // ------------------- POST -------------------
    public <T, R> T post(String uri, R body, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        return restClient.post()
                .uri(URI.create(uri))
                .contentType(MediaType.APPLICATION_JSON)
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .body(body)
                .retrieve()
                .body(responseType);

    }

    public <T> T postMultipartFile(String uri, MultiValueMap<String, Object> parts,
                                   Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.post()
                .uri(URI.create(uri))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .body(parts)
                .retrieve()
                .body(responseType);

    }

    public <T, R> T postNoBody(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.post()
                .uri(URI.create(uri))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .retrieve()
                .body(responseType);

    }


    // ------------------- PATCH -------------------
    public <T, R> T patch(String uri, R body, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.patch()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .body(body)
                .retrieve()
                .body(responseType);

    }


    // body 없는 PATCH
    public <T> T patchNoBody(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.patch()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .retrieve()
                .body(responseType);

    }


    // ------------------- PUT -------------------
    public <T, R> T put(String uri, R body, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.put()
                .uri(URI.create(uri))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .body(body)
                .retrieve()
                .body(responseType);
    }

    public <T, R> T putNoBody(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.put()
                .uri(URI.create(uri))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .retrieve()
                .body(responseType);

    }

    // ------------------- DELETE -------------------
    public <T> T delete(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        return restClient.delete()
                .uri(URI.create(uri))
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .cookies(cookies -> {
                    String accessSessionId =
                            (String) request.getAttribute("NEW_SES");

                    if (accessSessionId == null) {
                        accessSessionId = getAccessTokenFromCookie(request);
                    }

                    if (accessSessionId != null) {
                        cookies.add("SES", accessSessionId);
                    }
                })
                .cookies(cookies -> {
                    String refreshToken =
                            (String) request.getAttribute("NEW_AUT");

                    if (refreshToken == null) {
                        refreshToken = getRefreshTokenFromCookie(request);
                    }
                    if (refreshToken != null) {
                        cookies.add("AUT", refreshToken);
                    }
                })
                .retrieve()
                .body(responseType);
    }

    private String extractGuestId(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("guest_id".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }


    // ---------------- 현재 request 가져오기 ----------------
    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }


    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("AUT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("SES".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
