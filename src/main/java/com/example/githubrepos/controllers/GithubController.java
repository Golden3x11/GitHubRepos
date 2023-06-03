package com.example.githubrepos.controllers;

import com.example.githubrepos.services.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.githubrepos.rest.RestControllerUtil.xmlInNotSupportedResponse;

@RestController
@RequestMapping("/api/github")
public class GithubController {
    private final static String NON_ACCEPT_XML = "XML response is not supported";

    @Autowired
    private GithubService githubService;

    @GetMapping("/non-forked-repositories/{username}")
    public ResponseEntity<?> getRepositories(
            @PathVariable String username,
            @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader
    ) {
        if (acceptHeader.equals(MediaType.APPLICATION_XML_VALUE)) {
            return xmlInNotSupportedResponse();
        }

        var repositoryInfoList = githubService.getNonForkedRepositories(username);
        return ResponseEntity.ok(repositoryInfoList);
    }

}
