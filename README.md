The application will start on `http://localhost:8080`.

## API Endpoints

### Get Non-Forked Repositories

Retrieves a list of non-forked repositories and their branches for a given GitHub username.

**URL**: `/api/github/non-forked-repositories/{username}`

**Method**: GET

**Parameters**:
- `username` (path variable): The GitHub username to retrieve repositories for.

**Headers**:
- `Accept`: Set the `Accept` header to `application/json` to receive a JSON response.

**Response**:
- Status Code: 200 (OK)
- Body: A JSON array of repository objects, each containing the repository name, owner, and a list of branches.

## Error Handling

The API handles the following error scenarios:

- User not found: If the provided GitHub username does not exist, the API will return a `404 Not Found` response.
- Unsupported Accept header: If the `Accept` header is set to `application/xml`, the API will return a `406 Not Acceptable` response.

## Technologies Used

- Java
- Spring Boot
- Maven
