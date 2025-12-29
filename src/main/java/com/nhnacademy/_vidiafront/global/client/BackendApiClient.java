package com.nhnacademy._vidiafront.global.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackendApiClient {
    private final RestClient restClient;
    private final String AUTH = "/api/v1/auth";
    private final ObjectMapper objectMapper;


    // ------------------- GET -------------------
    public <T> T get(String uri, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);
        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);
        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;

        RestClient.RequestHeadersSpec<?> requestSpec = restClient.get()
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
                });
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);

    }

    // ------------------- POST -------------------
    public <T, R> T post(String uri, R body, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.post()
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
                .body(body);
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);


    }

    public <T> T postMultipartFile(String uri, MultiValueMap<String, Object> parts,
                                   ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.post()
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
                .body(parts);

        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);


    }

    public <T, R> T postNoBody(String uri, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.post()
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
                });
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);

    }


    // ------------------- PATCH -------------------
    public <T, R> T patch(String uri, R body, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.patch()
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
                .body(body);
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);

    }


    // body 없는 PATCH
    public <T> T patchNoBody(String uri, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.patch()
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
                });
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);


    }


    // ------------------- PUT -------------------
    public <T, R> T put(String uri, R body, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.put()
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
                .body(body);
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);

    }

    public <T, R> T putNoBody(String uri, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.put()
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
                });
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);

    }

    public <T> T putMultipartFile(String uri, MultiValueMap<String, Object> parts,
                                  Class<T> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.put()
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
                .body(parts);

        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return requestSpec
                .retrieve()
                .body(responseType);

    }

    // ------------------- DELETE -------------------
    public <T> T delete(String uri, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        log.info("{}호출", uri);

        HttpServletRequest request = getRequest();
        String guestId = extractGuestId(request);

        Object traceObj = MDC.get("traceId");
        String traceId = (traceObj instanceof String) ? (String) traceObj : null;
        RestClient.RequestHeadersSpec<?> requestSpec = restClient.delete()
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
                });
        if (traceId != null) {
            requestSpec = requestSpec.header("X-Trace-Id", traceId);
        }

        return exchange(requestSpec, responseType);

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

    private <T> T exchange(
            RestClient.RequestHeadersSpec<?> spec,
            ParameterizedTypeReference<ApiResponse<T>> typeRef
    ) {
        String body;

        try {
            log.debug("[BackendApiClient] requestSpec = {}", spec);

            // ✅ 1. 무조건 String으로 먼저 받는다
            body = spec.retrieve().body(String.class);

            log.debug("[BackendApiClient] raw response body = {}", body);

        } catch (RestClientResponseException e) {
            int status = e.getStatusCode().value();
            String errorBody = e.getResponseBodyAsString();

            log.warn(
                    "[BackendApiClient] HTTP error from backend. status={}, body={}",
                    status, errorBody
            );

            ApiResponse.Header h = tryParseHeaderFromBody(errorBody);

            if (h != null) {
                throw new ApiRequestException(
                        h.resultCode(),
                        h.resultMessage(),
                        h.errorCode() != null ? h.errorCode() : "UPSTREAM_ERROR"
                );
            }

            throw new ApiRequestException(
                    status,
                    "서버 응답이 올바르지 않습니다.",
                    "NON_JSON"
            );
        }

        // ✅ 2. 여기부터는 "HTTP 성공"이든 "논리 실패"든 동일 처리
        ApiResponse<T> response;

        try {
            JavaType javaType = objectMapper.getTypeFactory()
                    .constructType(typeRef.getType());

            response = objectMapper.readValue(body, javaType);

        } catch (Exception e) {
            log.error("[BackendApiClient] JSON parse error. body={}", body, e);
            throw new ApiRequestException(
                    500,
                    "응답 파싱에 실패했습니다.",
                    "RESPONSE_PARSE_ERROR"
            );
        }


        if (response == null || response.header() == null) {
            log.error("[BackendApiClient] EMPTY_RESPONSE");
            throw new ApiRequestException(
                    502,
                    "응답이 비어있습니다.",
                    "EMPTY_RESPONSE"
            );
        }

        log.debug(
                "[BackendApiClient] header.isSuccessful={}, resultCode={}, errorCode={}, message={}",
                response.header().isSuccessful(),
                response.header().resultCode(),
                response.header().errorCode(),
                response.header().resultMessage()
        );

        // ✅ 3. 논리 실패 (TEMP / DORMANT 포함)
        if (!response.header().isSuccessful()) {
            throw new ApiRequestException(
                    response.header().resultCode(),
                    response.header().resultMessage(),
                    response.header().errorCode()
            );
        }

        log.debug("[BackendApiClient] success, returning data");
        return response.data();
    }


    private ApiResponse.Header tryParseHeaderFromBody(String body) {
        if (body == null || body.isBlank()) return null;

        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode header = root.get("header");
            if (header == null || header.isNull()) return null;

            boolean isSuccessful = header.path("isSuccessful").asBoolean(true);
            int resultCode = header.path("resultCode").asInt(500);
            String resultMessage = header.path("resultMessage").asText("요청에 실패했습니다.");
            String errorCode = header.path("errorCode").isMissingNode() ? null : header.path("errorCode").asText(null);

            return new ApiResponse.Header(isSuccessful, resultCode, resultMessage, errorCode, null);

        } catch (Exception ignore) {
            return null;
        }
    }
}



