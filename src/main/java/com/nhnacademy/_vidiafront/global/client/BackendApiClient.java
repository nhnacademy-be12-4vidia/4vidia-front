package com.nhnacademy._vidiafront.global.client;

import com.nhnacademy._vidiafront.global.exception.ApiRequestException;
import com.nhnacademy._vidiafront.user.dto.auth.response.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BackendApiClient {
    private final RestClient restClient;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final String AUTH = "/api/v1/auth";

    // ------------------- GET -------------------
    public <T> T get(String uri, Class<T> responseType) {
        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();

        try {
            return restClient.get()
                    .uri(uri)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.get()
                        .uri(uri)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    public <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {

        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();

        try {
            return restClient.get()
                    .uri(uri)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .retrieve()
                    .body(typeReference);

        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.get()
                        .uri(uri)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .retrieve()
                        .body(typeReference);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed: " + ex.getMessage(), ex);
        }
    }
    // ------------------- POST -------------------
    public <T, R> T post(String uri, R body, Class<T> responseType) {
        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();
        try {
            return restClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.post()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .body(body)
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    public <T, R> T postNoBody(String uri, Class<T> responseType) {
        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();
        try {
            return restClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.post()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    // ------------------- PUT -------------------
    public <T, R> T put(String uri, R body, Class<T> responseType) {
        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();
        try {
            return restClient.put()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.put()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .body(body)
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }
    public <T, R> T putNoBody(String uri, Class<T> responseType) {
        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();
        try {
            return restClient.put()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.put()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    // ------------------- DELETE -------------------
    public <T> T delete(String uri, Class<T> responseType) {
        String token = (String) request.getSession().getAttribute("accessToken");
        String guestId = extractGuestId();
        try {
            return restClient.delete()
                    .uri(uri)
                    .header("Authorization", token != null ? "Bearer " + token : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue();
            if (tokenResponse != null) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.delete()
                        .uri(uri)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    // ------------------- Reissue helper -------------------
    private TokenResponse reissue() {
        try {
            return restClient.post()
                    .uri(AUTH + "/auth/reissue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(TokenResponse.class);
        } catch (HttpClientErrorException.Unauthorized ex) {
            return null;
        }
    }
    private String extractGuestId() {
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
}
