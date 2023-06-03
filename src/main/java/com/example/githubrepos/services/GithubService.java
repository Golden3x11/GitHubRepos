package com.example.githubrepos.services;

import com.example.githubrepos.dto.GithubRepoInfo;

import java.util.List;

public interface GithubService {
    List<GithubRepoInfo> getNonForkedRepositories(String username);
}
