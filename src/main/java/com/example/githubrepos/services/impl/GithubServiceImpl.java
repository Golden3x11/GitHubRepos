package com.example.githubrepos.services.impl;

import com.example.githubrepos.dto.GithubBranch;
import com.example.githubrepos.dto.GithubBranchInfo;
import com.example.githubrepos.dto.GithubRepo;
import com.example.githubrepos.dto.GithubRepoInfo;
import com.example.githubrepos.exeptions.DataRetrievalException;
import com.example.githubrepos.exeptions.UserNotFoundException;
import com.example.githubrepos.rest.RestRequestUtil;
import com.example.githubrepos.services.GithubService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GithubServiceImpl implements GithubService {
    private final static String GIT_HUB_URL = "https://api.github.com";
    private final static String GIT_HUB_USER_REPO_URL = GIT_HUB_URL + "/users/%s/repos";
    private final static String GIT_HUB_USER_REPO_BRANCHES_URL = GIT_HUB_URL + "/repos/%s/%s/branches";
    private final static String ERROR_NO_USER_FOUND = "Not found the user";
    private final static String ERROR_WHILE_RETRIEVING_REPOSITORIES = "Failed to retrieve repositories: ";
    private final static String ERROR_WHILE_RETRIEVING_BRANCHES = "Failed to retrieve branches: ";


    private final RestRequestUtil restRequestUtil;

    @Autowired
    public GithubServiceImpl(RestRequestUtil restRequestUtil) {
        this.restRequestUtil = restRequestUtil;
    }


    public List<GithubRepoInfo> getNonForkedRepositories(@NotNull String username) {
        List<GithubRepo> repositories = getRepositories(username);

        return repositories.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    List<GithubBranchInfo> branchList = getBranches(repo.owner().login(), repo.name());
                    return new GithubRepoInfo(repo.name(), repo.owner().login(), branchList);
                }).collect(Collectors.toList());
    }

    @NotNull
    private List<GithubRepo> getRepositories(@NotNull String username) {
        String repositoriesUrl = String.format(GIT_HUB_USER_REPO_URL, username);

        HttpHeaders headers = restRequestUtil.createHeaderAcceptJson();

        try {
            ResponseEntity<List<GithubRepo>> responseEntity = restRequestUtil.makeGetRequest(
                    repositoriesUrl, headers, new ParameterizedTypeReference<List<GithubRepo>>() {
                    });

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                throw new DataRetrievalException(ERROR_WHILE_RETRIEVING_REPOSITORIES + responseEntity.getBody());
            }

        } catch (HttpClientErrorException.NotFound ex) {
            throw new UserNotFoundException(ERROR_NO_USER_FOUND);

        } catch (HttpClientErrorException ex) {
            throw new DataRetrievalException(ERROR_WHILE_RETRIEVING_REPOSITORIES + ex.getMessage());
        }
    }

    @NotNull
    private List<GithubBranchInfo> getBranches(@NotNull String owner, @NotNull String repositoryName) {
        String branchesUrl = String.format(GIT_HUB_USER_REPO_BRANCHES_URL, owner, repositoryName);

        HttpHeaders headers = restRequestUtil.createHeaderAcceptJson();

        ResponseEntity<List<GithubBranch>> responseEntity = restRequestUtil.makeGetRequest(
                branchesUrl, headers, new ParameterizedTypeReference<List<GithubBranch>>() {
                });

        try {

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody().stream()
                        .map(branch -> new GithubBranchInfo(branch.name(), branch.commit().sha()))
                        .collect(Collectors.toList());
            } else {
                throw new DataRetrievalException(ERROR_WHILE_RETRIEVING_BRANCHES + responseEntity.getBody());
            }
        } catch (HttpClientErrorException ex) {
            throw new DataRetrievalException(ERROR_WHILE_RETRIEVING_BRANCHES + ex.getMessage());
        }
    }

}
