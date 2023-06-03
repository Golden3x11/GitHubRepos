package com.example.githubrepos.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class RestControllerUtil {
    private final static String NON_ACCEPT_XML = "XML response is not supported";

    public static ResponseEntity<?> xmlInNotSupportedResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .headers(headers)
                .body(generateXmlErrorResponse(HttpStatus.NOT_ACCEPTABLE.value()));

    }

    private static String generateXmlErrorResponse(int statusCode) {

        String xmlResponse = "<error>"
                + "<status>" + statusCode + "</status>"
                + "<message>" + NON_ACCEPT_XML + "</message>"
                + "</error>";

        return xmlResponse;
    }
}
