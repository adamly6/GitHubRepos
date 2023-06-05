package com.app.service;

import com.app.dto.BranchDTO;
import com.app.dto.RepositoryDTO;
import com.app.model.Branch;
import com.app.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class GithubRepoMapperService {

    private final WebClient webClient;
    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<RepositoryDTO> mapToDtoList(List<Repository> repositories) {
        return repositories.stream()
                .filter(repository -> !repository.fork())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RepositoryDTO mapToDto(Repository repository) {
        List<BranchDTO> branchDTOs = getBranches(repository);
        return new RepositoryDTO(
                repository.name(),
                repository.owner().login(),
                branchDTOs
        );
    }

    private List<BranchDTO> getBranches(Repository repository) {
        String url = GITHUB_API_URL + "/repos/" + repository.owner().login() +
                "/" + repository.name() + "/branches";

        List<Branch> branches = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Branch.class)
                .collectList()
                .block();

        return branches.stream()
                .map(branch -> new BranchDTO(branch.name(), branch.commit().sha()))
                .collect(Collectors.toList());
    }

    private BranchDTO mapToBranchDto(Branch branch) {
        return new BranchDTO(branch.name(), branch.commit().sha());
    }
}
