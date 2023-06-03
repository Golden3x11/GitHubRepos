package com.example.githubrepos.dto;

public record GithubRepo(String name, boolean fork, GithubUser owner) {
}
