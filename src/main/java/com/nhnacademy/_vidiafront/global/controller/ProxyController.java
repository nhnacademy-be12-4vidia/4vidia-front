package com.nhnacademy._vidiafront.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/proxy")
public class ProxyController {


    private RestClient externalRestClient;

    public ProxyController(@Qualifier("externalRestClient") RestClient externalRestClient) {
        this.externalRestClient = externalRestClient;
    }

    @GetMapping
    public ResponseEntity<byte[]> proxy(@RequestParam("url") String url) {
        byte[] body = externalRestClient.get()
            .uri(url)
            .retrieve()
            .body(byte[].class);

        String contentType = externalRestClient.get()
            .uri(url)
            .retrieve()
            .toEntity(byte[].class)
            .getHeaders()
            .getContentType()
            .toString();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType)
            .body(body);
    }

}
