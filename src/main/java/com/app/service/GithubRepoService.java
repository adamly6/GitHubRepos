package com.app.service;

import com.app.exception.UserNotFoundException;
import com.app.dto.BranchDTO;
import com.app.dto.RepositoryDTO;
import com.app.model.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubRepoService {

    private static final String GITHUB_API_URL = "https://api.github.com";

    private final WebClient webClient;
    private final GithubRepoMapperService mapperService;

    public List<RepositoryDTO> getRepositories(String username) throws UserNotFoundException {
        String url = GITHUB_API_URL + "/users/" + username + "/repos";

        List<Repository> repositories = webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new UserNotFoundException()))
                .bodyToFlux(Repository.class)
                .collectList()
                .block();

        if (repositories == null) {
            throw new UserNotFoundException();
        }

        return mapperService.mapToDtoList(repositories);
    }
}

