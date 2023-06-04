## Github Repositories API

This is a simple API that allows you to retrieve a list of Github repositories for a given user.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

- Java 17 or above
- Maven installed

### Installing

1. Clone the repository:

   ```
   git clone https://github.com/adamly6/GitHubRepos.git
   ```
   
2. Build the project:

	```
	cd projectDirectory
	mvn clean install
	```

3. Run the application:

    ```
	java -jar target/app-1.0.jar
	```
	
## Usage
### Retrieve repositories
To retrieve repositories for a specific user, make a GET request to the following endpoint:

	
	GET /repositories/{username}
	

Replace {username} with the desired Github username. The API will respond with a JSON array containing the repositories and their information.

Example for GET /repositories/adamly6


	[
		{
			"repoName": "adamly6",
			"owner": "adamly6",
			"branches": [
				{
					"name": "main",
					"lastCommitSha": "2ecc0b5a8412af8af95fe0195f89365d5aac5626"
				}
			]
		}
	]


## Error Handling
If the specified user does not exist, the API will return a 404 status code with an error message in the response body.


	{
		"status": 404,
		"message": "User not found"
	}

If the request specifies header “Accept: application/xml”, the API will return a 406 status code with an error message.


	{
		"status": 406,
		"message": "Unsupported media type"
	}
