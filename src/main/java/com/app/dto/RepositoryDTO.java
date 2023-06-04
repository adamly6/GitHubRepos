package com.app.dto;

import java.util.List;

public record RepositoryDTO(String repoName, String owner, List<BranchDTO> branches) {
}

