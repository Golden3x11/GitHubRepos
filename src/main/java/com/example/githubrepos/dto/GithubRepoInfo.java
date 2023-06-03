package com.example.githubrepos.dto;

import java.util.List;

public record GithubRepoInfo(String repositoryName, String ownerLogin, List<GithubBranchInfo> branches) {
}