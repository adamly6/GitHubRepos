package com.app.controller;

import com.app.dto.RepositoryDTO;
import com.app.service.GithubRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;




import java.util.List;

@RestController
@RequiredArgsConstructor
public class GithubRepoController {

    private final GithubRepoService githubRepoService;


    @GetMapping(value = "/repositories/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryDTO> getRepositories(
            @PathVariable String username
    )
    {
        return githubRepoService.getRepositories(username);
    }
}