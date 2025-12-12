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
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.get()
                    .uri(URI.create(uri))
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                return restClient.get()
                        .uri(uri)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    public <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.get()
                    .uri(URI.create(uri))
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .retrieve()
                    .body(typeReference);

        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.get()
                        .uri(URI.create(uri))
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .retrieve()
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
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.post()
                    .uri(URI.create(uri))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.post()
                        .uri(URI.create(uri))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .body(body)
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    public <T> T postMultipartFile(String uri, MultiValueMap<String, Object> parts,
        Class<T> responseType) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.post()
                .uri(URI.create(uri))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                .header("X-Guest-Id", guestId != null ? guestId : "")
                .header("Cookie", refreshToken != null ? "refresh=" + refreshToken : "")
                .body(parts)
                .retrieve()
                .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.post()
                    .uri(URI.create(uri))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .header("Authorization", "Bearer " + tokenResponse.accessToken())
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .header("Cookie", refreshToken != null ? "refresh=" + refreshToken : "")
                    .body(parts)
                    .retrieve()
                    .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend multipart request failed: " + ex, ex);
        }
    }

    public <T, R> T postNoBody(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.post()
                    .uri(URI.create(uri))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.post()
                        .uri(URI.create(uri))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }



    // ------------------- PATCH -------------------
    public <T, R> T patch(String uri, R body, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.patch()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })
                    .body(body)
                    .retrieve()
                    .body(responseType);

        } catch (HttpClientErrorException.Unauthorized ex) {

            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);

            if (isReissue) {

                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());

                return restClient.patch()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })
                        .body(body)
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }

        } catch (Exception ex) {
            throw new ApiRequestException("Backend PATCH request failed: " + ex, ex);
        }
    }


    // body 없는 PATCH
    public <T> T patchNoBody(String uri, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.patch()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })
                    .retrieve()
                    .body(responseType);

        } catch (HttpClientErrorException.Unauthorized ex) {

            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);

            if (isReissue) {

                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());

                return restClient.patch()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })
                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }

        } catch (Exception ex) {
            throw new ApiRequestException("Backend PATCH NoBody request failed: " + ex, ex);
        }
    }










    // ------------------- PUT -------------------
    public <T, R> T put(String uri, R body, Class<T> responseType) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.put()
                    .uri(URI.create(uri))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.put()
                        .uri(URI.create(uri))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .body(body)
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
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.put()
                    .uri(URI.create(uri))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.put()
                        .uri(URI.create(uri))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .retrieve()
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
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute("accessToken") : null;
        String guestId = extractGuestId(request);
        String refreshToken = getRefreshTokenFromCookie(request);

        try {
            return restClient.delete()
                    .uri(URI.create(uri))
                    .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                    .header("X-Guest-Id", guestId != null ? guestId : "")
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            TokenResponse tokenResponse = reissue(refreshToken);
            boolean isReissue = reissueIfNeeded(tokenResponse, request, response);
            if (isReissue) {
                request.getSession(true).setAttribute("accessToken", tokenResponse.accessToken());
                return restClient.delete()
                        .uri(URI.create(uri))
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .header("X-Guest-Id", guestId != null ? guestId : "")
                        .cookies(cookies -> {
                            if (refreshToken != null) {
                                cookies.add("refresh", refreshToken);
                            }
                        })                        .retrieve()
                        .body(responseType);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw new ApiRequestException("Backend request failed"+ex.toString(), ex);
        }
    }

    // ------------------- Reissue helper -------------------
    private TokenResponse reissue(String refreshToken) {
        try {
            return restClient.post()
                    .uri(AUTH + "/auth/reissue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookies(cookies -> {
                        if (refreshToken != null) {
                            cookies.add("refresh", refreshToken);
                        }
                    })
                    .retrieve()
                    .body(TokenResponse.class);
        } catch (HttpClientErrorException.Unauthorized ex) {
            return null;
        }
    }
    private boolean reissueIfNeeded(TokenResponse tokenResponse, HttpServletRequest request, HttpServletResponse response) {

        if (tokenResponse == null) {
            return false;
        }

        // 1) accessToken 다시 세션에 저장
        HttpSession session = request.getSession(true);
        if (session != null) {
            session.setAttribute("accessToken", tokenResponse.accessToken());
        }

        // 2) refreshToken 다시 쿠키에 저장
        Cookie refreshCookie = new Cookie("refresh", tokenResponse.refreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        return true;
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

    // ---------------- 현재 response 가져오기 ----------------
    private HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();
    }
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
