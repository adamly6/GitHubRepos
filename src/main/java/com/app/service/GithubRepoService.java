package com.app.service;

import com.app.exception.UserNotFoundException;
import com.app.dto.BranchDTO;
import com.app.dto.RepositoryDTO;
import com.app.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubRepoService {

    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<RepositoryDTO> getRepositories(String username) throws UserNotFoundException {
        String url = GITHUB_API_URL + "/users/" + username + "/repos";
        RestTemplate restTemplate = new RestTemplate();
        Repository[] repositories;

        try {
            repositories = restTemplate.getForObject(url, Repository[].class);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }


        List<RepositoryDTO> repositoryDTOs = new ArrayList<>();

        for (Repository repository : repositories) {
            if (!repository.isFork()) {
                List<BranchDTO> branchDTOs = getBranches(repository);
                RepositoryDTO repositoryDTO = new RepositoryDTO(
                        repository.getName(),
                        repository.getOwner().getLogin(),
                        branchDTOs
                );
                repositoryDTOs.add(repositoryDTO);
            }
        }

        return repositoryDTOs;
    }

    private List<BranchDTO> getBranches(Repository repository) {
        String url = GITHUB_API_URL + "/repos/" + repository.getOwner().getLogin() +
                "/" + repository.getName() + "/branches";
        RestTemplate restTemplate = new RestTemplate();
        Branch[] branches = restTemplate.getForObject(url, Branch[].class);
        List<BranchDTO> branchDTOs = new ArrayList<>();

        for (Branch branch : branches) {
            BranchDTO branchDTO = new BranchDTO(branch.getName(), branch.getCommit().getSha());
            branchDTOs.add(branchDTO);
        }

        return branchDTOs;
    }
}

