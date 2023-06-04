package com.app;

import com.app.controller.GithubRepoController;
import com.app.dto.BranchDTO;
import com.app.dto.RepositoryDTO;
import com.app.exception.UserNotFoundException;
import com.app.service.GithubRepoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(GithubRepoController.class)
class GitHubRepoApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GithubRepoService githubRepoService;


	@Test
	public void testGetRepositories_UserNotFound() throws Exception {
		// Arrange
		String username = "nonexistentuser";

		given(githubRepoService.getRepositories(username))
				.willThrow(new UserNotFoundException());

		// Act and Assert
		mockMvc.perform(get("/repositories/{username}", username)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("$.message").value("User not found"));
	}

	@Test
	public void testGetRepositories_UnsupportedMediaType() throws Exception {
		// Arrange
		String username = "testuser";

		// Act and Assert
		mockMvc.perform(get("/repositories/{username}", username)
						.accept(MediaType.APPLICATION_XML))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.status").value(HttpStatus.NOT_ACCEPTABLE.value()))
				.andExpect(jsonPath("$.message").value("Unsupported media type"));
	}


	@Test
	public void testGetRepositories_Success() throws Exception {
		// Arrange
		String username = "testuser";
		List<RepositoryDTO> expectedRepositories = List.of(
				new RepositoryDTO("repo1", "user1", List.of(new BranchDTO("branch1", "commit1"))),
				new RepositoryDTO("repo2", "user1", List.of(new BranchDTO("branch2", "commit2")))
		);

		given(githubRepoService.getRepositories(username))
				.willReturn(expectedRepositories);

		// Act and Assert
		mockMvc.perform(get("/repositories/{username}", username)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].repoName").value("repo1"))
				.andExpect(jsonPath("$[0].owner").value("user1"))
				.andExpect(jsonPath("$[0].branches[0].name").value("branch1"))
				.andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("commit1"))
				.andExpect(jsonPath("$[1].repoName").value("repo2"))
				.andExpect(jsonPath("$[1].owner").value("user1"))
				.andExpect(jsonPath("$[1].branches[0].name").value("branch2"))
				.andExpect(jsonPath("$[1].branches[0].lastCommitSha").value("commit2"));
	}



}
