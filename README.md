# 10 - Authentication between services

## Authentication

### Why do we need to have authentication between services?

Authentication between services is essential for ensuring that only authorized services can access other services. It is a security measure that helps to prevent unauthorized access to sensitive data and resources. Even if we have a secured network that our services run (are not available to the public), we still need to have authentication between services in case the attacker gains access to the network.

### Type of authentication

| Authentication requirement | Authentication mechanism                        | Usage                                                                                     |
|----------------------------|-------------------------------------------------|-------------------------------------------------------------------------------------------|
| Username and password      | Basic, Form-based authentication                | Common for web applications, providing a straightforward way for users to log in.         |
| Bearer access token        | OIDC Bearer token authentication, JWT, OAuth2   | Used for API authentication, enabling secure access with tokens instead of credentials.   |
| Client certificate         | Mutual TLS authentication                       | Utilized for machine-to-machine authentication where both client and server are verified. |
| Single sign-on (SSO)       | OIDC Code Flow, SAML, Form-based authentication | Allows users to log in once and access multiple systems without re-authenticating.        |

#### Basic authentication

Basic authentication is a simple authentication scheme built into the HTTP protocol. The client sends HTTP requests with the Authorization header that contains the word Basic followed by a space and a base64-encoded string username:password. The server decodes the base64-encoded string and uses the username and password to authenticate the user.

The problem with basic authentication is that the username and password are sent in every request, increasing the risk of being stolen.

#### Bearer access token

Bearer access token is a type of access token that is used to authenticate API requests. The client sends HTTP requests with the Authorization header that contains the word Bearer followed by a space and a token. The server uses the token to authenticate the user.

A token can be issued after successful login using Basic authentication or SSO. The token can be used to access other services without the need to provide a username and password again. It's useful for user authentication, so he doesn't need to write a username and password for every request.

However, the problem with the token invalidation comes. If the token is stolen, the attacker can use it until it expires. To mitigate this problem, the token can be short-lived, and the client can use a refresh token to get a new token.

#### Mutual TLS authentication

Mutual TLS authentication is a type of authentication that uses client certificates to authenticate the client. The client sends HTTP requests with the client certificate. The server uses the client certificate to authenticate the client.

#### Single sign-on (SSO)

Single sign-on (SSO) is a type of authentication that allows users to log in once and access multiple systems without re-authenticating. The client sends HTTP requests with the Authorization header that contains the word Bearer followed by a space and a token. The server uses the token to authenticate the user against the identity provider.

### HTTPS

When the application is running in a production environment, it's important to use HTTPS to secure the communication between the client and the server. HTTPS is a secure version of HTTP that uses SSL/TLS to encrypt the data transmitted between the client and the server. It provides a secure channel for the client and the server to communicate over the internet. **Always use HTTPS in a production environment.** However, we can use HTTP in our development environment.

### Examples for Basic authentication

```java
@Path("/api/public")
public class PublicResource {

    @GET
    @PermitAll // allow all access
    @Produces(MediaType.TEXT_PLAIN)
    public String publicResource() {
        return "public";
   }
   
    @GET
    @RolesAllowed("user") // allow only users with role "user" to access this resource
    @Path("/me")
    public String me(@Context SecurityContext securityContext) { // inject the security context
      return securityContext.getUserPrincipal().getName();
    }

    @GET
    @RolesAllowed("admin") // allow only users with role "admin" to access this resource 
    @Produces(MediaType.TEXT_PLAIN)
    public String adminResource() {
      return "admin";
    }
}
```

## State of the project

- Added `security-jpa-reactive` extension to and `baggage-service`.
- In `baggage-service` added `User` entity for managing users for basic authentication. Check `User` entity in `baggage-service` for more details.

## Tasks

### 1. Introduction to the task

In this task, we will secure the communication between the services using basic authentication. We will focus on REST API security between services and not on the security of the REST API to the client (e.g. web FE for managing flights,...).

### 2. Add Basic authentication to `baggages-service`

#### 2.1. Add Basic authentication to `GET /baggage/passenger/{passengerId}`

1. Check the `User` entity. It's used to manage users for basic authentication.
2. Add annotations to the `BaggageResource` class to secure the `GET /baggage/passenger/{passengerId}` endpoint with basic authentication only for role `"user"`.


During the startup, the `User` entity is populated with the following user:
- username: `passenger-service`
- password: `secret`

#### 2.2. Test it

1. Run the `baggage-service`.
2. Authenticate with the username `passenger-service` and password `secret` to access the `GET /baggage/passenger/{passengerId}` endpoint using swagger.
3. Try to access the `GET /baggage/passenger/{passengerId}` endpoint. It should return ok or fail with 500 (simulation of fault tolerance), but not 401.


### 3. Use username and password in `passenger-service` to access `baggage-service`

In the `baggage-service`, there is a new user with username `passenger-service` and password `secret`. Use this username and password in the `passenger-service` to access the `baggage-service` endpoint.

#### 3.1. Set username and password in `application.properties` in `passenger-service`

Go to the `passenger-service` and set the username and password in the `application.properties` file. Follow the TODOs.

#### 3.2. Use username and password in `passenger-service` to access `baggage-service`

Go to `BaggageClientCustomHeaders` and add an Authorization header with the username and password to the request. Follow the TODOs.

#### 3.3. Test it

1. Run the `baggage-service` and `passenger-service`.
2. Create a new passenger in the `passenger-service`.
3. Get the passenger with baggage. It shouldn't fail with 401 nor 500.

### 4. Submit the solution

1. Finish the tasks
2. Push the changes to the main branch
3. GitHub Classroom automatically prepared a feedback pull request for you
4. Go to the repository on GitHub and find the feedback pull request
5. Set label to "Submitted"
6. GitHub Actions will run basic checks for your submission
7. Teacher will evaluate the submission as well and give you feedback

## Hints

- Build and run docker
  ```bash
  cd passenger-service && mvn clean install && cd ../baggage-service && mvn clean install && cd ../flight-service && mvn clean install && cd .. && docker compose up --build
   ```

## Troubleshooting

- Check if your docker engine is running.

## Further reading

- https://quarkus.io/guides/security-basic-authentication
- https://quarkus.io/guides/security-authentication-mechanisms#basic-authentication
- https://quarkus.io/guides/security-getting-started-tutorial