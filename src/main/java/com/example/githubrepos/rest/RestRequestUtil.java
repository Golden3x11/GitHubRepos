package com.example.githubrepos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestRequestUtil {

    private final RestTemplate restTemplate;

    @Autowired
    public RestRequestUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpHeaders createHeaderAcceptJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public <T> ResponseEntity<T> makeGetRequest(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }
}
